package soat.project.hackaton_soat_manager.application.output.video.get;

import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

public record GetVideoByProcessIdOutput(
        String processId,
        String userId,
        String status,
        String createdAt,
        String processedAt
) {
    public static GetVideoByProcessIdOutput from(VideoProcessing video) {
        return new GetVideoByProcessIdOutput(
                video.getId().getValue(),
                video.getUserId().getValue(),
                video.getStatus().name(),
                video.getCreatedAt().toString(),
                video.getProcessedAt().toString()
        );
    }
}
