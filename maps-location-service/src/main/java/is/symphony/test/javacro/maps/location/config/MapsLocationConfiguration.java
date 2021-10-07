package is.symphony.test.javacro.maps.location.config;

import is.symphony.test.javacro.maps.location.properties.MapsLocationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class MapsLocationConfiguration {
    private final WebClient.Builder webClientBuilder;
    private final MapsLocationProperties mapsLocationProperties;

    public MapsLocationConfiguration(final WebClient.Builder webClientBuilder,
                                     final MapsLocationProperties mapsLocationProperties) {
        this.webClientBuilder = webClientBuilder;
        this.mapsLocationProperties = mapsLocationProperties;
    }


    @Bean
    public WebClient webClient() {
        return webClientBuilder
                .baseUrl(mapsLocationProperties.getUrl())
                .defaultHeaders(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .build();
    }
}
