package soat.project.hackaton_soat_manager.infrastructure.video.presenter;


import soat.project.hackaton_soat_manager.application.output.video.upload.UploadVideoOutput;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;

public class UploadVideoPresenter {

    public static UploadVideoResponse present(UploadVideoOutput output) {
        return new UploadVideoResponse(
                output.processId(),
                output.status()
        );
    }
}
