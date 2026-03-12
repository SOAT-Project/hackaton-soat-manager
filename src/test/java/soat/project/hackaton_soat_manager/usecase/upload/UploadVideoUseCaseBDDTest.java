package soat.project.hackaton_soat_manager.usecase.upload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soat.project.hackaton_soat_manager.application.command.video.upload.UploadVideoCommand;
import soat.project.hackaton_soat_manager.application.gateway.ProcessingQueueGateway;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.upload.UploadVideoUseCaseImpl;
import soat.project.hackaton_soat_manager.domain.exception.DomainException;
import soat.project.hackaton_soat_manager.domain.video.VideoName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("BDD - Upload Video Use Case")
class UploadVideoUseCaseBDDTest {

    private VideoProcessingGateway videoGateway;
    private StorageGateway storageGateway;
    private ProcessingQueueGateway queueGateway;

    private UploadVideoUseCaseImpl useCase;

    @BeforeEach
    void setup() {

        videoGateway = mock(VideoProcessingGateway.class);
        storageGateway = mock(StorageGateway.class);
        queueGateway = mock(ProcessingQueueGateway.class);

        useCase = new UploadVideoUseCaseImpl(
                videoGateway,
                storageGateway,
                queueGateway,
                "videos"
        );
    }

    @Test
    @DisplayName("Given valid video file, When uploading video, Then video is stored and processing is requested")
    void givenValidVideo_whenUpload_thenVideoStoredAndProcessingRequested() {

        // ===== GIVEN =====
        final var command = new UploadVideoCommand(
                "user1",
                "video.mp4",
                VideoName.of("video.mp4"),
                "video/mp4",
                new byte[]{1,2,3}
        );

        // ===== WHEN =====
        final var output = useCase.execute(command);

        // ===== THEN =====
        assertNotNull(output);
        assertEquals("PENDING", output.status());

        verify(storageGateway).upload(any(), any(), any(), any());
        verify(videoGateway).save(any());
        verify(queueGateway).sendProcessingMessage(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Given invalid file extension, When uploading video, Then domain exception is thrown")
    void givenInvalidExtension_whenUpload_thenThrowException() {

        // ===== GIVEN =====
        final var command = new UploadVideoCommand(
                "user1",
                "video.txt",
                VideoName.of("video.txt"),
                "text/plain",
                new byte[]{1}
        );

        // ===== WHEN / THEN =====
        assertThrows(
                DomainException.class,
                () -> useCase.execute(command)
        );

        verifyNoInteractions(storageGateway);
        verifyNoInteractions(queueGateway);
    }

    @Test
    @DisplayName("Given queue failure, When uploading video, Then video is marked as failure")
    void givenQueueFailure_whenUpload_thenVideoMarkedAsFailure() {

        // ===== GIVEN =====
        final var command = new UploadVideoCommand(
                "user1",
                "video.mp4",
                VideoName.of("video.mp4"),
                "video/mp4",
                new byte[]{1,2,3}
        );

        doThrow(new RuntimeException("queue error"))
                .when(queueGateway)
                .sendProcessingMessage(any(), any(), any(), any());

        // ===== WHEN / THEN =====
        assertThrows(
                RuntimeException.class,
                () -> useCase.execute(command)
        );

        verify(videoGateway, times(2)).save(any());
    }
}