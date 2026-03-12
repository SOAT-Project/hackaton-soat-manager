package soat.project.hackaton_soat_manager.usecase.download;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import soat.project.hackaton_soat_manager.application.command.video.download.DownloadVideoCommand;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.download.DownloadVideoUseCaseImpl;
import soat.project.hackaton_soat_manager.domain.exception.VideoProcessingFailedException;
import soat.project.hackaton_soat_manager.domain.exception.VideoStillProcessingException;
import soat.project.hackaton_soat_manager.domain.video.*;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("BDD - Download Video Use Case")
class DownloadVideoUseCaseBDDTest {

    private VideoProcessingGateway videoGateway;
    private StorageGateway storageGateway;

    private DownloadVideoUseCaseImpl useCase;

    @BeforeEach
    void setup() {

        videoGateway = mock(VideoProcessingGateway.class);
        storageGateway = mock(StorageGateway.class);

        useCase = new DownloadVideoUseCaseImpl(
                videoGateway,
                storageGateway
        );
    }

    @Test
    @DisplayName("Given processed video, When requesting download, Then download URL is returned")
    void givenProcessedVideo_whenDownload_thenReturnUrl() {

        // ===== GIVEN =====
        final var processId = ProcessId.generate();

        final var video = VideoProcessing.rehydrate(
                processId,
                UserId.of("user1"),
                VideoName.of("video"),
                VideoStatus.PROCESSED,
                "bucket",
                "file/key",
                null,
                Instant.now(),
                Instant.now(),
                "video.mp4",
                100
        );

        when(videoGateway.findByProcessId(processId))
                .thenReturn(Optional.of(video));

        when(storageGateway.generateDownloadUrl("bucket", "file/key"))
                .thenReturn("http://download");

        final var command = new DownloadVideoCommand(processId.getValue());

        // ===== WHEN =====
        final var output = useCase.execute(command);

        // ===== THEN =====
        assertNotNull(output);
        assertEquals("http://download", output.downloadUrl());
        assertEquals("video.mp4", output.fileName());
    }

    @Test
    @DisplayName("Given pending video, When requesting download, Then still processing exception is thrown")
    void givenPendingVideo_whenDownload_thenThrowStillProcessingException() {

        // ===== GIVEN =====
        final var processId = ProcessId.generate();

        final var video = VideoProcessing.create(
                UserId.of("user1"),
                processId,
                VideoName.of("video"),
                "video.mp4",
                100,
                "bucket",
                "path"
        );

        when(videoGateway.findByProcessId(processId))
                .thenReturn(Optional.of(video));

        final var command = new DownloadVideoCommand(processId.getValue());

        // ===== WHEN / THEN =====
        assertThrows(
                VideoStillProcessingException.class,
                () -> useCase.execute(command)
        );
    }

    @Test
    @DisplayName("Given failed video processing, When requesting download, Then processing failed exception is thrown")
    void givenFailedVideo_whenDownload_thenThrowProcessingFailedException() {

        // ===== GIVEN =====
        final var processId = ProcessId.generate();

        final var video = VideoProcessing.rehydrate(
                processId,
                UserId.of("user1"),
                VideoName.of("video"),
                VideoStatus.FAILURE,
                "bucket",
                "file/key",
                "processing error",
                Instant.now(),
                Instant.now(),
                "video.mp4",
                100
        );

        when(videoGateway.findByProcessId(processId))
                .thenReturn(Optional.of(video));

        final var command = new DownloadVideoCommand(processId.getValue());

        // ===== WHEN / THEN =====
        assertThrows(
                VideoProcessingFailedException.class,
                () -> useCase.execute(command)
        );
    }

    @Test
    @DisplayName("Given video not found, When requesting download, Then exception is thrown")
    void givenVideoNotFound_whenDownload_thenThrowException() {

        // ===== GIVEN =====
        final var processId = ProcessId.generate();

        when(videoGateway.findByProcessId(processId))
                .thenReturn(Optional.empty());

        final var command = new DownloadVideoCommand(processId.getValue());

        // ===== WHEN / THEN =====
        assertThrows(
                RuntimeException.class,
                () -> useCase.execute(command)
        );
    }
}