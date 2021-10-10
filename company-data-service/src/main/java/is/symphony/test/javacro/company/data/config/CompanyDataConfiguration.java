package is.symphony.test.javacro.company.data.config;

import is.symphony.test.javacro.company.data.handler.CompanyDataHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.inject.Inject;

import static is.symphony.test.javacro.company.data.config.CompanyDataConstants.COMPANY_DATA_LIST_PATH;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@EnableConfigurationProperties
public class CompanyDataConfiguration {
    private final CompanyDataHandler companyDataHandler;

    @Inject
    public CompanyDataConfiguration(final CompanyDataHandler companyDataHandler) {
        this.companyDataHandler = companyDataHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> companyDataRouter() {
        return route(GET(COMPANY_DATA_LIST_PATH).and(accept(APPLICATION_NDJSON)), companyDataHandler::getAll)
                .and(route(POST(COMPANY_DATA_LIST_PATH).and(accept(APPLICATION_NDJSON)), companyDataHandler::saveAll));
    }
}
