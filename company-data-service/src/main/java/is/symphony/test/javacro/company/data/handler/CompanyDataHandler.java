package is.symphony.test.javacro.company.data.handler;

import is.symphony.test.javacro.company.data.generated.api.model.Company;
import is.symphony.test.javacro.company.data.service.CompanyDataListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Named;

import java.time.Duration;

import static org.springframework.http.MediaType.*;

@Named
public class CompanyDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDataHandler.class);

    private final CompanyDataListService companyDataListService;

    public CompanyDataHandler(final CompanyDataListService companyDataListService) {
        this.companyDataListService = companyDataListService;
    }

    public Mono<ServerResponse> getAll(final ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_NDJSON)
                .body(companyDataListService.getAllCompanies(), Company.class);
    }

    public Mono<ServerResponse> saveAll(final ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(companyDataListService.saveAll(request.bodyToFlux(Company.class)), Void.class);
    }
}
