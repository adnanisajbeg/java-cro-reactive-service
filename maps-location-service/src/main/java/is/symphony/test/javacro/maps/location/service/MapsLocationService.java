package is.symphony.test.javacro.maps.location.service;

import is.symphony.test.javacro.maps.location.config.MapsLocationConfiguration;
import is.symphony.test.javacro.maps.location.generated.api.model.MapsLocationData;
import is.symphony.test.javacro.maps.location.properties.MapsLocationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class MapsLocationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MapsLocationService.class);

    private final WebClient webClient;
    private final MapsLocationProperties mapsLocationProperties;

    public MapsLocationService(final WebClient webClient,
                               final MapsLocationProperties mapsLocationProperties) {
        this.webClient = webClient;
        this.mapsLocationProperties = mapsLocationProperties;
    }

    public Flux<byte[]> getLocationList(Flux<MapsLocationData> mapsLocationDataFlux) {
        return mapsLocationDataFlux
                .flatMap(data -> getLocation(Mono.just(data)));
    }

    public Mono<byte[]> getLocation(Mono<MapsLocationData> mapsLocationData) {
        return mapsLocationData
                .flatMap(data -> webClient
                    .get()
                    .uri(uri -> uri.queryParam("apiKey", mapsLocationProperties.getApiKey())
                            .queryParam("co", data.getCountry())
                            .queryParam("z", 17)
                            .queryParam("i", 1)
                            .queryParam("ci", data.getCity())
                            .queryParam("s", data.getAddress())
                            .queryParam("n", data.getAddressNumber())
                            .queryParam("w", data.getImageWidth()).build())
                        .accept(MediaType.IMAGE_JPEG)
                        .retrieve()
                        .onStatus(HttpStatus::isError, response -> response.bodyToMono(Error.class) // error body as String or other class
                                .flatMap(error -> {
                                    LOGGER.error("Issue with Dataiku API call {}", error);
                                    return Mono.just(error);
                                })
                                .flatMap(error -> Mono.error(new RuntimeException(error.getMessage())))) // throw a function
                        .bodyToMono(byte[].class)
                        .retryWhen(Retry.backoff(mapsLocationProperties.getRetry(),
                                Duration.ofSeconds(mapsLocationProperties.getBackoffSeconds())))
                );
    }


}
