package soat.project.hackaton_soat_manager.infrastructure.web.s3;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.InputStream;
import java.time.Duration;

@Component
public class S3StorageGateway implements StorageGateway {

    private final S3Client s3Client;
    private final String defaultBucket;
    private final S3Presigner s3Presigner;

    public S3StorageGateway(
            final S3Client s3Client,
            @Value("${aws.s3.bucket}") final String defaultBucket,
            final S3Presigner s3Presigner
    ) {
        this.s3Client = s3Client;
        this.defaultBucket = defaultBucket;
        this.s3Presigner = s3Presigner;
    }

    @Override
    public String upload(String key, InputStream inputStream, long contentLength, String contentType) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(defaultBucket)
                .key(key)
                .contentType(contentType)
                .contentLength(contentLength)
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromInputStream(inputStream, contentLength)
        );

        return key;
    }

    @Override
    public void upload(String bucket, String key, byte[] content, String contentType) {

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(contentType)
                .contentLength((long) content.length)
                .build();

        s3Client.putObject(
                request,
                RequestBody.fromBytes(content)
        );
    }

    @Override
    public String generateDownloadUrl(String bucket, String key) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        PresignedGetObjectRequest presignedRequest =
                s3Presigner.presignGetObject(r -> r
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(getObjectRequest)
                );

        return presignedRequest.url().toString();
    }
}