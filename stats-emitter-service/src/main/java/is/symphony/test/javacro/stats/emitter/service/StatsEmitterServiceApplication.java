package is.symphony.test.javacro.stats.emitter.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class StatsEmitterServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StatsEmitterServiceApplication.class, args);
    }
}
