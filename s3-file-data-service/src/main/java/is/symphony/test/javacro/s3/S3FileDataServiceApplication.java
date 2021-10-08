package is.symphony.test.javacro.s3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class S3FileDataServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(S3FileDataServiceApplication.class, args);
    }
}
