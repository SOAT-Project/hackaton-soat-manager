package soat.project.hackaton_soat_manager.application.gateway;

import java.io.InputStream;

public interface StorageGateway {

    String upload(String key, InputStream inputStream, long contentLength, String contentType);

    void upload(
            String bucket,
            String key,
            byte[] content,
            String contentType
    );

    String generateDownloadUrl(String bucket, String key);

}
