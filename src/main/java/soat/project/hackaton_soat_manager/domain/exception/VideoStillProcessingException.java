package soat.project.hackaton_soat_manager.domain.exception;

import soat.project.hackaton_soat_manager.domain.validation.DomainError;

import java.util.List;


public class VideoStillProcessingException extends DomainException {

    public VideoStillProcessingException() {
        super(
                "Video is still processing",
                List.of(new DomainError("Video is still processing"))
        );
    }
}
