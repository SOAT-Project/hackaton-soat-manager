package soat.project.hackaton_soat_manager.infrastructure.video.model.response;

import soat.project.hackaton_soat_manager.application.output.video.list.ListVideosByUserOutput;

import java.util.List;

public record ListVideosByUserResponse(
        List<ListVideoItemResponse> videos
) {

    public static ListVideosByUserResponse from(
            ListVideosByUserOutput output
    ) {

        var items = output.videos().stream()
                .map(video -> new ListVideoItemResponse(
                        video.processId(),
                        video.videoName(),
                        video.status(),
                        video.createdAt(),
                        video.processedAt()
                ))
                .toList();

        return new ListVideosByUserResponse(items);
    }
}
