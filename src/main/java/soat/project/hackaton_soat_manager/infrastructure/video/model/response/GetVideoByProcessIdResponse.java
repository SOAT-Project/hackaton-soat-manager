package soat.project.hackaton_soat_manager.infrastructure.video.model.response;

public record GetVideoByProcessIdResponse(
        String processId,
        String userId,
        String status,
        String createdAt,
        String processedAt
) {
}
