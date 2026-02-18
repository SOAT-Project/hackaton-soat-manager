package soat.project.hackaton_soat_manager.application.output.video.list;

import java.time.Instant;

public record VideoListItem(
        String processId,
        String videoName,
        String status,
        Instant createdAt,
        Instant processedAt
) {
}
