package soat.project.hackaton_soat_manager.infrastructure.video.presenter;

import soat.project.hackaton_soat_manager.application.output.video.download.DownloadVideoOutput;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.DownloadVideoResponse;

public class DownloadVideoPresenter {

    public static DownloadVideoResponse present(DownloadVideoOutput output) {
        return new DownloadVideoResponse(
                output.downloadUrl(),
                output.expiresAt(),
                output.fileName()
        );
    }
}
