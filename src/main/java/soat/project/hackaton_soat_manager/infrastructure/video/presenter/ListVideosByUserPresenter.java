package soat.project.hackaton_soat_manager.infrastructure.video.presenter;


import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.ListVideoItemResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.ListVideosByUserResponse;

import java.util.List;

public class ListVideosByUserPresenter {

    public static ListVideosByUserResponse present(
            List<VideoProcessing> videos
    ) {

        var items = videos.stream()
                .map(video -> new ListVideoItemResponse(
                        video.getId().getValue(),
                        video.getVideoName().getValue(),
                        video.getStatus().name(),
                        video.getCreatedAt(),
                        video.getProcessedAt()
                ))
                .toList();

        return new ListVideosByUserResponse(items);
    }
}