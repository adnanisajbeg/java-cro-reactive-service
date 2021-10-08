package is.symphony.test.javacro.s3.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import is.symphony.test.javacro.s3.generated.api.model.QueueMessage;
import is.symphony.test.javacro.s3.properties.S3FileDataServiceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;

@Service
public class SqsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsService.class);

    private final SqsAsyncClient sqsAsyncClient;
    private final S3FileDataServiceProperties s3FileDataServiceProperties;
    private final ObjectMapper objectMapper;
    private final S3PullService s3PullService;
    private final FileProcessingService fileProcessingService;

    public SqsService(final SqsAsyncClient sqsAsyncClient,
                      final S3FileDataServiceProperties s3FileDataServiceProperties,
                      final S3PullService s3PullService,
                      final FileProcessingService fileProcessingService) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.s3FileDataServiceProperties = s3FileDataServiceProperties;
        this.s3PullService = s3PullService;
        this.fileProcessingService = fileProcessingService;

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    @PostConstruct
    public void continuousListener() {
        LOGGER.info("Started listener... {}", s3FileDataServiceProperties.getQueueUrl());

        Mono<ReceiveMessageResponse> receiveMessageResponseMono = Mono.fromFuture(() ->
                sqsAsyncClient.receiveMessage(
                        ReceiveMessageRequest.builder()
                                .maxNumberOfMessages(5)
                                .waitTimeSeconds(10)
                                .visibilityTimeout(30)
                                .queueUrl(s3FileDataServiceProperties.getQueueUrl())
                                .build()
                )
        );

        receiveMessageResponseMono
                .repeat()
                .retry()
                .map(ReceiveMessageResponse::messages)
                .map(Flux::fromIterable)
                .flatMap(messageFlux -> messageFlux)

                .flatMap(e -> Mono.fromCallable(() ->
                        objectMapper.readValue(e.body(), QueueMessage.class))
                        .zipWith(Mono.just(e)))


                .flatMap(tuple2 -> processNewFile(tuple2.getT1()).zipWith(Mono.just(tuple2.getT2())))
                .log()
                .subscribe(tuple2 -> {
                    LOGGER.info("message: {} ", tuple2.getT1());
/**
                    sqsAsyncClient.deleteMessage(DeleteMessageRequest
                                    .builder()
                                    .queueUrl(s3FileDataServiceProperties.getQueueUrl())
                                    .receiptHandle(tuple2.getT2().receiptHandle())
                                    .build())
                            .thenAccept(deleteMessageResponse -> {
                                LOGGER.info("deleted message with handle {}", tuple2.getT2().receiptHandle());
                            }); */
                });
    }

    private Mono<QueueMessage> processNewFile(QueueMessage queueMessage) {
        LOGGER.info("Processing new notification {}", queueMessage);
        if (queueMessage.getRecords() == null) {
            return Mono.just(queueMessage);
        }

        return Mono.just(queueMessage.getRecords())
                .flatMapMany(Flux::fromIterable)
                .map(e -> e.getS3().getObject().getKey())
         /**       .map(e -> {
                    LOGGER.info("Part size: {}", e.length());
                    return e;
                }) */
                .flatMap(s3PullService::getFileForAsset)
                .map(e -> new String(e.array(), StandardCharsets.UTF_8))
                .collectList()
                .flatMap(e -> fileProcessingService.sendDataToProcessing(String.join("", e)))
                .thenReturn(queueMessage);
    }
}
