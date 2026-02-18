package soat.project.hackaton_soat_manager.application.output.video.download;

import java.time.Instant;

public record DownloadVideoOutput(
        String downloadUrl,
        Instant expiresAt,
        String fileName
) {
}
