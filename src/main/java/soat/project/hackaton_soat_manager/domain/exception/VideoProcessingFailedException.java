package soat.project.hackaton_soat_manager.domain.exception;

import soat.project.hackaton_soat_manager.domain.validation.DomainError;

import java.util.List;

public class VideoProcessingFailedException extends DomainException {

    public VideoProcessingFailedException(String errorMessage) {
        super(
                "Video processing failed",
                List.of(new DomainError(errorMessage))
        );
    }
}

