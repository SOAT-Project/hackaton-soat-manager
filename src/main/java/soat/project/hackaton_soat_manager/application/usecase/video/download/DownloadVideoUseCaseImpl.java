package soat.project.hackaton_soat_manager.application.usecase.video.download;

import soat.project.hackaton_soat_manager.application.command.video.download.DownloadVideoCommand;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.output.video.download.DownloadVideoOutput;
import soat.project.hackaton_soat_manager.domain.exception.VideoProcessingFailedException;
import soat.project.hackaton_soat_manager.domain.exception.VideoStillProcessingException;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.VideoStatus;

import java.time.Duration;
import java.time.Instant;

public class DownloadVideoUseCaseImpl extends DownloadVideoUseCase{

    public final VideoProcessingGateway videoProcessingGateway;
    public final StorageGateway storageGateway;

    public DownloadVideoUseCaseImpl(
            final VideoProcessingGateway videoProcessingGateway,
            final StorageGateway storageGateway
    ) {
        this.videoProcessingGateway = videoProcessingGateway;
        this.storageGateway = storageGateway;
    }

    @Override
    public DownloadVideoOutput execute(DownloadVideoCommand command) {

        var processId = ProcessId.of(command.processId());

        var video = videoProcessingGateway.findByProcessId(processId)
                .orElseThrow(() -> new RuntimeException("Video not found"));

        if (video.getStatus() == VideoStatus.PENDING) {
            throw new VideoStillProcessingException();
        }

        if (video.getStatus() == VideoStatus.FAILURE) {
            throw new VideoProcessingFailedException(video.getErrorMessage());
        }


        String key = video.getFilePath();

        String url = storageGateway.generateDownloadUrl(
                video.getFileBucket(),
                key
        );

        return new DownloadVideoOutput(
                url,
                Instant.now().plus(Duration.ofMinutes(10)),
                video.getFileName()
        );
    }
}
