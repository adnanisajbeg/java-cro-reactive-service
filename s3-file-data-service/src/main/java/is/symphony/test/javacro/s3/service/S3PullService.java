package is.symphony.test.javacro.s3.service;

import is.symphony.test.javacro.s3.properties.S3FileDataServiceProperties;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.SdkPublisher;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

@Service
public class S3PullService {
    private final S3AsyncClient s3AsyncClient;
    private final S3FileDataServiceProperties s3FileDataServiceProperties;
    private final FileProcessingService fileProcessingService;

    public S3PullService(final S3AsyncClient s3AsyncClient,
                         final S3FileDataServiceProperties s3FileDataServiceProperties,
                         final FileProcessingService fileProcessingService) {
        this.s3AsyncClient = s3AsyncClient;
        this.s3FileDataServiceProperties = s3FileDataServiceProperties;
        this.fileProcessingService = fileProcessingService;
    }

    @PostConstruct
    public void data() {
        getAllFileNamesFromBucket().log().subscribe();
    }

    

    public Flux<ByteBuffer> getFileForAsset(final String key) {

        return Mono.fromFuture(
                        s3AsyncClient
                                .getObject(GetObjectRequest.builder()
                                                .bucket(s3FileDataServiceProperties.getBucket())
                                                .key(key)
                                                .build(),
                                        new FluxResponseProvider()))
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to get data from S3 bucket!")))
                .flatMapMany(FluxResponse::getFlux);
    }

    public Flux<S3Object> getAllFileNamesFromBucket() {
        return Mono.fromFuture(s3AsyncClient
                        .listObjects(ListObjectsRequest
                                .builder()
                                .bucket(s3FileDataServiceProperties.getBucket())
                                .build()))
                .map(ListObjectsResponse::contents)
                .doOnError(e -> Mono.error(new RuntimeException("Better error message")))
                .flatMapIterable(list -> list);
    }





    static class FluxResponseProvider implements AsyncResponseTransformer<GetObjectResponse, FluxResponse> {

        private FluxResponse response;

        @Override
        public CompletableFuture<FluxResponse> prepare() {
            response = new FluxResponse();
            return response.cf;
        }

        @Override
        public void onResponse(final GetObjectResponse sdkResponse) {
            this.response.sdkResponse = sdkResponse;
        }

        @Override
        public void onStream(final SdkPublisher<ByteBuffer> publisher) {
            response.flux = Flux.from(publisher);
            response.cf.complete(response);
        }

        @Override
        public void exceptionOccurred(final Throwable error) {
            response.cf.completeExceptionally(error);
        }

    }

    /**
     * Holds the API response and stream
     * @author Philippe
     */
    static class FluxResponse {

        final CompletableFuture<FluxResponse> cf = new CompletableFuture<>();
        private GetObjectResponse sdkResponse;
        private Flux<ByteBuffer> flux;

        public CompletableFuture<FluxResponse> getCf() {
            return cf;
        }

        public GetObjectResponse getSdkResponse() {
            return sdkResponse;
        }

        public FluxResponse setSdkResponse(final GetObjectResponse sdkResponse) {
            this.sdkResponse = sdkResponse;
            return this;
        }

        public Flux<ByteBuffer> getFlux() {
            return flux;
        }

        public FluxResponse setFlux(final Flux<ByteBuffer> flux) {
            this.flux = flux;
            return this;
        }
    }
}
