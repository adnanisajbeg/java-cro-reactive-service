package is.symphony.test.javacro.s3.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import is.symphony.test.javacro.s3.generated.api.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Collectors;

@Service
public class FileProcessingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingService.class);

    private final ObjectMapper mapper;

    public FileProcessingService() {
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Mono<Void> sendDataToProcessing(String fileData) {
        LOGGER.info("processing file data...");

        return Flux.fromStream(fileData::lines)
                .parallel(4)
                .flatMap(e -> Mono.fromCallable(() -> mapper.readValue(e, Company.class)))
                .log()
                .doOnNext(this::sendCompany)
                .then();
    }

    private void sendCompany(Company e) {   // TODO: send over kafka
        LOGGER.info("sending company... {}", e);
    }
}
