package is.symphony.test.javacro.s3.config;

import is.symphony.test.javacro.s3.properties.S3FileDataServiceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsAsyncClientBuilder;
import software.amazon.awssdk.utils.StringUtils;

import java.time.Duration;

@Configuration
public class S3FileDataServiceConfiguration {
    private final S3FileDataServiceProperties s3FileDataServiceProperties;
    private final WebClient.Builder webClientBuilder;

    @Value("${company.data.url}")
    private String companyDataURL;

    public S3FileDataServiceConfiguration(final S3FileDataServiceProperties s3FileDataServiceProperties,
                                          final WebClient.Builder webClientBuilder) {
        this.s3FileDataServiceProperties = s3FileDataServiceProperties;
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    public WebClient webClient() {
        return webClientBuilder
                .baseUrl(companyDataURL)
                .defaultHeaders(headers -> headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_NDJSON_VALUE))
                .build();
    }

    @Bean
    public S3AsyncClient s3client() {

        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ZERO)
                .maxConcurrency(10)
                .build();

        S3Configuration serviceConfiguration = S3Configuration.builder()
                .checksumValidationEnabled(false)
                .chunkedEncodingEnabled(true)
                .build();

        S3AsyncClientBuilder b = S3AsyncClient.builder().httpClient(httpClient)
                .region(Region.of(s3FileDataServiceProperties.getRegion()))
                .credentialsProvider(awsCredentialsProvider())
                .serviceConfiguration(serviceConfiguration);

        return b.build();
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        if (StringUtils.isBlank(s3FileDataServiceProperties.getAccessKeyId())) {
            return DefaultCredentialsProvider.create();
        } else {
            return () -> AwsBasicCredentials.create(
                    s3FileDataServiceProperties.getAccessKeyId(),
                    s3FileDataServiceProperties.getSecretAccessKey());
        }
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        SdkAsyncHttpClient httpClient = NettyNioAsyncHttpClient.builder()
                .writeTimeout(Duration.ZERO)
                .maxConcurrency(10)
                .build();

        SqsAsyncClientBuilder builder = SqsAsyncClient
                .builder()
                .httpClient(httpClient)
                .region(Region.of(s3FileDataServiceProperties.getRegion()))
                .credentialsProvider(awsCredentialsProvider());


        return builder.build();
    }

}
