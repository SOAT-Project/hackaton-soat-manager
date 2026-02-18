package soat.project.hackaton_soat_manager.infrastructure.video.model.response;

import java.time.Instant;

public record DownloadVideoResponse(
        String downloadUrl,
        Instant expiresAt,
        String fileName
) {
}
