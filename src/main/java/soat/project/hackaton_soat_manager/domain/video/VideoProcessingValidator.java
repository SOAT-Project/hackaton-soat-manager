package soat.project.hackaton_soat_manager.domain.video;

import soat.project.hackaton_soat_manager.domain.validation.DomainError;
import soat.project.hackaton_soat_manager.domain.validation.ValidationHandler;
import soat.project.hackaton_soat_manager.domain.validation.Validator;

public class VideoProcessingValidator extends Validator {

    private final VideoProcessing video;

    public VideoProcessingValidator(
            final VideoProcessing video,
            final ValidationHandler handler
    ) {
        super(handler);
        this.video = video;
    }

    @Override
    public void validate() {
        checkMandatoryFields();
        checkProcessedRules();
        checkFailureRules();
        checkPendingRules();
    }

    private void checkMandatoryFields() {
        if (video.getUserId() == null) {
            validationHandler().append(new DomainError("'userId' must not be null"));
        }

        if (video.getVideoName() == null) {
            validationHandler().append(new DomainError("'videoName' must not be null"));
        }

        if (video.getStatus() == null) {
            validationHandler().append(new DomainError("'status' must not be null"));
        }
    }

    private void checkProcessedRules() {
        if (video.getStatus() == VideoStatus.PROCESSED) {

            if (video.getFileBucket() == null) {
                validationHandler().append(new DomainError("'fileBucket' must be provided when PROCESSED"));
            }

            if (video.getFilePath() == null) {
                validationHandler().append(new DomainError("'filePath' must be provided when PROCESSED"));
            }

            if (video.getProcessedAt() == null) {
                validationHandler().append(new DomainError("'processedAt' must exist when PROCESSED"));
            }
        }
    }

    private void checkFailureRules() {
        if (video.getStatus() == VideoStatus.FAILURE) {

            if (video.getErrorMessage() == null) {
                validationHandler().append(new DomainError("'errorMessage' must be provided when FAILURE"));
            }

            if (video.getProcessedAt() == null) {
                validationHandler().append(new DomainError("'processedAt' must exist when FAILURE"));
            }
        }
    }

    private void checkPendingRules() {
        if (video.getStatus() == VideoStatus.PENDING) {

            if (video.getErrorMessage() != null) {
                validationHandler().append(
                        new DomainError("'errorMessage' must be null when PENDING")
                );
            }
        }
    }
}
