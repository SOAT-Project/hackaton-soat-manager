package soat.project.hackaton_soat_manager.usecase.download;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soat.project.hackaton_soat_manager.application.command.video.download.DownloadVideoCommand;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.download.DownloadVideoUseCaseImpl;
import soat.project.hackaton_soat_manager.domain.exception.VideoStillProcessingException;
import soat.project.hackaton_soat_manager.domain.video.*;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DownloadVideoUseCaseTest {

    @Mock
    private VideoProcessingGateway videoGateway;

    @Mock
    private StorageGateway storageGateway;

    @InjectMocks
    private DownloadVideoUseCaseImpl useCase;

    @Test
    void shouldDownloadVideoSuccessfully() {

        var processId = ProcessId.generate();

        var video = VideoProcessing.rehydrate(
                processId,
                UserId.of("user1"),
                VideoName.of("video"),
                VideoStatus.PROCESSED,
                "bucket",
                "file/key",
                null,
                Instant.now(),
                Instant.now(),
                "file.mp4",
                100
        );

        when(videoGateway.findByProcessId(processId))
                .thenReturn(Optional.of(video));

        when(storageGateway.generateDownloadUrl("bucket", "file/key"))
                .thenReturn("http://download");

        var command = new DownloadVideoCommand(processId.getValue());

        var output = useCase.execute(command);

        assertThat(output.downloadUrl()).isEqualTo("http://download");
        assertThat(output.fileName()).isEqualTo("file.mp4");
    }

    @Test
    void shouldThrowWhenVideoNotFound() {

        var processId = ProcessId.generate();

        when(videoGateway.findByProcessId(processId))
                .thenReturn(Optional.empty());

        var command = new DownloadVideoCommand(processId.getValue());

        assertThrows(RuntimeException.class,
                () -> useCase.execute(command));
    }

    @Test
    void shouldThrowWhenVideoStillProcessing() {

        var processId = ProcessId.generate();

        var video = VideoProcessing.create(
                UserId.of("user1"),
                processId,
                VideoName.of("video"),
                "file.mp4",
                100,
                "bucket",
                "path"
        );

        when(videoGateway.findByProcessId(processId))
                .thenReturn(Optional.of(video));

        var command = new DownloadVideoCommand(processId.getValue());

        assertThrows(VideoStillProcessingException.class,
                () -> useCase.execute(command));
    }
}