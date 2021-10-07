package is.symphony.test.javacro.maps.location.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("maps.location")
public class MapsLocationProperties {
    private String url;
    private String apiKey;
    private Long retry = 10L;
    private Long backoffSeconds = 1L;

    public String getUrl() {
        return url;
    }

    public MapsLocationProperties setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getApiKey() {
        return apiKey;
    }

    public MapsLocationProperties setApiKey(String apiKey) {
        this.apiKey = apiKey;
        return this;
    }

    public Long getRetry() {
        return retry;
    }

    public MapsLocationProperties setRetry(Long retry) {
        this.retry = retry;
        return this;
    }

    public Long getBackoffSeconds() {
        return backoffSeconds;
    }

    public MapsLocationProperties setBackoffSeconds(Long backoffSeconds) {
        this.backoffSeconds = backoffSeconds;
        return this;
    }
}
