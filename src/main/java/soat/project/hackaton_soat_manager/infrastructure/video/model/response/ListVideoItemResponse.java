package soat.project.hackaton_soat_manager.infrastructure.video.model.response;

import java.time.Instant;

public record ListVideoItemResponse(
        String processId,
        String videoName,
        String status,
        Instant createdAt,
        Instant processedAt
) {
}
