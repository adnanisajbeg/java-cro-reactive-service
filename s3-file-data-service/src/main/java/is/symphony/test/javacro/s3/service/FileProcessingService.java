package is.symphony.test.javacro.s3.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import is.symphony.test.javacro.s3.generated.api.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FileProcessingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessingService.class);

    private final ObjectMapper mapper;
    private final WebClient webClient;

    public FileProcessingService(final WebClient webClient) {
        this.webClient = webClient;
        this.mapper = new ObjectMapper();
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Mono<Void> sendDataToProcessing(String fileData) {
        LOGGER.info("processing file data...");

        return sendCompany(Flux.fromStream(fileData::lines)
                    .flatMap(e -> Mono.fromCallable(() -> mapper.readValue(e, Company.class)))
                        .log()
                );
    }

    private Mono<Void> sendCompany(Flux<Company> companies) {
        LOGGER.info("sending companies...");

        return webClient
                .post()
                .body(companies, Company.class)
                .retrieve()
                .bodyToMono(Void.class);

    }
}
