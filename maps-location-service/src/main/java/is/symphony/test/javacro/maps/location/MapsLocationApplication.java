package is.symphony.test.javacro.maps.location;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class MapsLocationApplication {
    public static void main(String[] args) {
        SpringApplication.run(MapsLocationApplication.class, args);
    }
}
