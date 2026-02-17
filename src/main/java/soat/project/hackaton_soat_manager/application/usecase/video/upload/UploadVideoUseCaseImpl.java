package soat.project.hackaton_soat_manager.application.usecase.video.upload;

import soat.project.hackaton_soat_manager.application.command.video.upload.UploadVideoCommand;
import soat.project.hackaton_soat_manager.application.gateway.ProcessingQueueGateway;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.output.video.upload.UploadVideoOutput;
import soat.project.hackaton_soat_manager.domain.exception.NotificationException;
import soat.project.hackaton_soat_manager.domain.validation.handler.Notification;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.UserId;
import soat.project.hackaton_soat_manager.domain.video.VideoName;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

import java.time.Instant;

public class UploadVideoUseCaseImpl extends UploadVideoUseCase {

    private final String bucket;
    private final VideoProcessingGateway videoGateway;
    private final StorageGateway storageGateway;
    private final ProcessingQueueGateway queueGateway;

    public UploadVideoUseCaseImpl(
            final VideoProcessingGateway videoGateway,
            final StorageGateway storageGateway,
            final ProcessingQueueGateway queueGateway,
            final String bucket
    ) {
        this.videoGateway = videoGateway;
        this.storageGateway = storageGateway;
        this.queueGateway = queueGateway;
        this.bucket = bucket;
    }

    @Override
    public UploadVideoOutput execute(UploadVideoCommand command) {

        final Notification notification = Notification.create();

        validateExtension(command.fileName());

        final var processId = ProcessId.generate();
        final var userId = UserId.of(command.userId());

        final var key = "pending/%s/%s".formatted(
                processId.getValue(),
                Instant.now().toString()
        );

        storageGateway.upload(
                bucket,
                key,
                command.fileContent(),
                command.contentType()
        );

        final var video = notification.validate(() ->
                VideoProcessing.create(
                        userId,
                        processId,
                        VideoName.of(command.fileName()),
                        command.fileName(),
                        command.fileContent().length,
                        bucket,
                        key
                )
        );


        if (notification.hasError()) {
            notification.getErrors().forEach(err ->
                    System.out.println("Validation error: " + err.message())
            );
            throw new NotificationException("Upload failed", notification);
        }

        videoGateway.save(video);

        try {
            queueGateway.sendProcessingMessage(
                    processId.getValue(),
                    userId.getValue(),
                    bucket,
                    key
            );
        } catch (Exception ex) {

            video.markFailure("Failed to enqueue processing");
            videoGateway.save(video);

            throw ex;
        }

        return UploadVideoOutput.from(
                processId.getValue(),
                video.getStatus().name()
        );
    }


    private void validateExtension(String fileName) {
        if (!fileName.endsWith(".mp4")
                && !fileName.endsWith(".avi")
                && !fileName.endsWith(".mov")
                && !fileName.endsWith(".mkv")) {
            throw new IllegalArgumentException("Invalid video extension");
        }
    }
}
