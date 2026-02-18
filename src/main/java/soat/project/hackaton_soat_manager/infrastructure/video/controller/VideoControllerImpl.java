package soat.project.hackaton_soat_manager.infrastructure.video.controller;

import org.springframework.stereotype.Component;
import soat.project.hackaton_soat_manager.application.command.video.download.DownloadVideoCommand;
import soat.project.hackaton_soat_manager.application.command.video.get.GetVideoByProcessIdCommand;
import soat.project.hackaton_soat_manager.application.command.video.list.ListVideosByUserCommand;
import soat.project.hackaton_soat_manager.application.command.video.upload.UploadVideoCommand;
import soat.project.hackaton_soat_manager.application.usecase.video.download.DownloadVideoUseCase;
import soat.project.hackaton_soat_manager.application.usecase.video.get.GetVideoByProcessIdUseCase;
import soat.project.hackaton_soat_manager.application.usecase.video.list.ListVideosByUserUseCase;
import soat.project.hackaton_soat_manager.application.usecase.video.upload.UploadVideoUseCase;
import soat.project.hackaton_soat_manager.domain.video.VideoName;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.DownloadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.GetVideoByProcessIdResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.ListVideosByUserResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.presenter.DownloadVideoPresenter;
import soat.project.hackaton_soat_manager.infrastructure.video.presenter.GetVideoByProcessIdPresenter;
import soat.project.hackaton_soat_manager.infrastructure.video.presenter.UploadVideoPresenter;

@Component
public class VideoControllerImpl implements VideoController {

    private final UploadVideoUseCase uploadVideoUseCase;
    private final DownloadVideoUseCase downloadVideoUseCase;
    private final ListVideosByUserUseCase listVideosByUserUseCase;
    private final GetVideoByProcessIdUseCase getVideoByProcessIdUseCase;


    public VideoControllerImpl(
            final UploadVideoUseCase uploadVideoUseCase,
            final DownloadVideoUseCase downloadVideoUseCase,
            final ListVideosByUserUseCase listVideosByUserUseCase,
            final GetVideoByProcessIdUseCase getVideoByProcessIdUseCase) {
        this.uploadVideoUseCase = uploadVideoUseCase;
        this.downloadVideoUseCase = downloadVideoUseCase;
        this.listVideosByUserUseCase = listVideosByUserUseCase;
        this.getVideoByProcessIdUseCase = getVideoByProcessIdUseCase;
    }

    @Override
    public UploadVideoResponse upload(
            String userId,
            String fileName,
            String videoname,
            String contentType,
            byte[] fileContent
    ) {

        var command = UploadVideoCommand.with(
                userId,
                fileName,
                VideoName.of(videoname),
                contentType,
                fileContent
        );

        var output = uploadVideoUseCase.execute(command);

        return UploadVideoPresenter.present(output);
    }

    @Override
    public DownloadVideoResponse download(String processId) {

        var command = DownloadVideoCommand.with(processId);
        var output = downloadVideoUseCase.execute(command);
        return DownloadVideoPresenter.present(output);
    }

    @Override
    public ListVideosByUserResponse listByUser(String userId) {

        var command = ListVideosByUserCommand.with(userId);

        var output = listVideosByUserUseCase.execute(command);

        return ListVideosByUserResponse.from(output);
    }

    @Override
    public GetVideoByProcessIdResponse getByProcessId(String processId) {
        var command = GetVideoByProcessIdCommand.with(processId);
        var output =  getVideoByProcessIdUseCase.execute(command);
        return GetVideoByProcessIdPresenter.present(output);
    }

}