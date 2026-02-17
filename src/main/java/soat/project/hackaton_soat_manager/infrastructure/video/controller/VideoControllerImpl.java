package soat.project.hackaton_soat_manager.infrastructure.video.controller;

import org.springframework.stereotype.Component;
import soat.project.hackaton_soat_manager.application.command.video.upload.UploadVideoCommand;
import soat.project.hackaton_soat_manager.application.usecase.video.upload.UploadVideoUseCase;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.presenter.UploadVideoPresenter;

@Component
public class VideoControllerImpl implements VideoController {

    private final UploadVideoUseCase uploadVideoUseCase;

    public VideoControllerImpl(UploadVideoUseCase uploadVideoUseCase) {
        this.uploadVideoUseCase = uploadVideoUseCase;
    }

    @Override
    public UploadVideoResponse upload(
            String userId,
            String fileName,
            String contentType,
            byte[] fileContent
    ) {

        var command = UploadVideoCommand.with(
                userId,
                fileName,
                contentType,
                fileContent
        );

        var output = uploadVideoUseCase.execute(command);

        return UploadVideoPresenter.present(output);
    }

}