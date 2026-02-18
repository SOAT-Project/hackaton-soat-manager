package soat.project.hackaton_soat_manager.application.usecase.video.update;

import soat.project.hackaton_soat_manager.application.command.video.update.UpdateVideoStatusCommand;
import soat.project.hackaton_soat_manager.application.gateway.NotificationQueueGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.output.video.update.UpdateVideoStatusOutput;
import soat.project.hackaton_soat_manager.domain.exception.NotFoundException;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

public class UpdateVideoStatusUseCaseImpl extends UpdateVideoStatusUseCase {

    private final VideoProcessingGateway videoProcessingGateway;
    private final NotificationQueueGateway notificationQueueGateway;

    public UpdateVideoStatusUseCaseImpl(
            VideoProcessingGateway videoProcessingGateway,
            NotificationQueueGateway notificationQueueGateway
    ) {
        this.videoProcessingGateway = videoProcessingGateway;
        this.notificationQueueGateway = notificationQueueGateway;
    }

    @Override
    public UpdateVideoStatusOutput execute(UpdateVideoStatusCommand command) {

        var processId = ProcessId.of(command.processId());

        var video = videoProcessingGateway
                .findByProcessId(processId)
                .orElseThrow(() ->
                        NotFoundException.with(VideoProcessing.class, processId)
                );

        if (video.isFinished()) {
            return UpdateVideoStatusOutput.notUpdated(
                    video.getId().getValue(),
                    video.getStatus().name()
            );
        }

        if (command.isFailure()) {
            video.markFailure(command.errorMessage());
        } else {
            video.markProcessed(
                    command.fileBucket(),
                    command.fileKey()
            );
        }

        videoProcessingGateway.save(video);

        notificationQueueGateway.sendNotification(
                video.getUserId().getValue(),
                video.getStatus().name(),
                video.getErrorMessage(),
                video.getVideoName().getValue(),
                video.getId().getValue()
        );

        return UpdateVideoStatusOutput.updated(
                video.getId().getValue(),
                video.getStatus().name()
        );
    }


}
