package soat.project.hackaton_soat_manager.domain.video;


import soat.project.hackaton_soat_manager.domain.exception.NotificationException;
import soat.project.hackaton_soat_manager.domain.shared.AggregateRoot;
import soat.project.hackaton_soat_manager.domain.validation.ValidationHandler;
import soat.project.hackaton_soat_manager.domain.validation.handler.Notification;

import java.time.Instant;

public class VideoProcessing extends AggregateRoot<ProcessId> {

    private final UserId userId;
    private final VideoName videoName;
    private final String fileName;
    private final long fileSize;
    private VideoStatus status;
    private String fileBucket;
    private String filePath;
    private String errorMessage;
    private Instant processedAt;

    private VideoProcessing(
            final ProcessId id,
            final UserId userId,
            final VideoName videoName, String fileName, long fileSize,
            final VideoStatus status,
            final String fileBucket,
            final String filePath,
            final String errorMessage,
            final Instant createdAt,
            final Instant processedAt
    ) {
        super(id, createdAt);

        this.userId = userId;
        this.videoName = videoName;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.status = status;
        this.fileBucket = fileBucket;
        this.filePath = filePath;
        this.errorMessage = errorMessage;
        this.processedAt = processedAt;

        this.selfValidation();
    }

    public static VideoProcessing create(
            final UserId userId,
            final ProcessId processId,
            final VideoName videoName,
            final String fileName,
            final long fileSize,
            final String fileBucket,
            final String filePath
    ) {

        return new VideoProcessing(
                processId,
                userId,
                videoName,
                fileName,
                fileSize,
                VideoStatus.PENDING,
                fileBucket,
                filePath,
                null,
                Instant.now(),
                null
        );
    }

    public static VideoProcessing rehydrate(
            final ProcessId id,
            final UserId userId,
            final VideoName videoName,
            final VideoStatus status,
            final String fileBucket,
            final String filePath,
            final String errorMessage,
            final Instant createdAt,
            final Instant processedAt,
            final String fileName,
            final long fileSize
    ) {
        return new VideoProcessing(
                id,
                userId,
                videoName,
                fileName,
                fileSize,
                status,
                fileBucket,
                filePath,
                errorMessage,
                createdAt,
                processedAt

        );
    }

    public void markProcessed(String fileBucket, String filePath) {

        if (this.status == VideoStatus.PROCESSED) {
            return;
        }

        if (this.status == VideoStatus.FAILURE) {
            return;
        }

        this.status = VideoStatus.PROCESSED;
        this.fileBucket = fileBucket;
        this.filePath = filePath;
        this.processedAt = Instant.now();
    }

    public void markFailure(String errorMessage) {

        if (this.status == VideoStatus.FAILURE) {
            return;
        }

        if (this.status == VideoStatus.PROCESSED) {
            return;
        }

        this.status = VideoStatus.FAILURE;
        this.errorMessage = errorMessage;
        this.processedAt = Instant.now();
    }

    public boolean isFinished() {
        return this.status == VideoStatus.PROCESSED
                || this.status == VideoStatus.FAILURE;
    }

    @Override
    public void validate(ValidationHandler handler) {
        new VideoProcessingValidator(this, handler).validate();
    }

    private void selfValidation() {
        final var notification = Notification.create();

        this.validate(notification);

        if (notification.hasError()) {
            notification.getErrors().forEach(err ->
                    System.out.println("Domain validation error: " + err.message())
            );
            throw new NotificationException("failed to create a video processor", notification);
        }
    }

    public UserId getUserId() {
        return userId;
    }

    public VideoName getVideoName() {
        return videoName;
    }

    public VideoStatus getStatus() {
        return status;
    }

    public String getFileBucket() {
        return fileBucket;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }


}
