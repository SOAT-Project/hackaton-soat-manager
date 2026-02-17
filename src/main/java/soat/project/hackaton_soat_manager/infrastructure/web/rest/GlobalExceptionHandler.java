package soat.project.hackaton_soat_manager.infrastructure.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import soat.project.hackaton_soat_manager.domain.exception.DomainException;
import soat.project.hackaton_soat_manager.domain.exception.NotFoundException;
import soat.project.hackaton_soat_manager.domain.exception.VideoProcessingFailedException;
import soat.project.hackaton_soat_manager.domain.exception.VideoStillProcessingException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getErrors());
    }

    @ExceptionHandler(VideoStillProcessingException.class)
    public ResponseEntity<?> handleProcessing(VideoStillProcessingException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getErrors());
    }

    @ExceptionHandler(VideoProcessingFailedException.class)
    public ResponseEntity<?> handleFailure(VideoProcessingFailedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(ex.getErrors());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomain(DomainException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getErrors());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpected(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Unexpected error"));
    }
}

