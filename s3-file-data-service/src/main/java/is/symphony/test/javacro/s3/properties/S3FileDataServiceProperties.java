package is.symphony.test.javacro.s3.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("s3")
public class S3FileDataServiceProperties {
    private String region;
    private String accessKeyId;
    private String secretAccessKey;
    private String bucket;
    private String queueName;
    private String queueUrl;

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    public S3FileDataServiceProperties setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public S3FileDataServiceProperties setRegion(String region) {
        this.region = region;
        return this;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public S3FileDataServiceProperties setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public S3FileDataServiceProperties setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getQueueName() {
        return queueName;
    }

    public S3FileDataServiceProperties setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public String getQueueUrl() {
        return queueUrl;
    }

    public S3FileDataServiceProperties setQueueUrl(String queueUrl) {
        this.queueUrl = queueUrl;
        return this;
    }
}
