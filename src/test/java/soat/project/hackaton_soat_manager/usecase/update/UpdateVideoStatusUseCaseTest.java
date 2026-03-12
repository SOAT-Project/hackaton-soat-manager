package soat.project.hackaton_soat_manager.usecase.update;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soat.project.hackaton_soat_manager.application.command.video.update.UpdateVideoStatusCommand;
import soat.project.hackaton_soat_manager.application.gateway.NotificationQueueGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.update.UpdateVideoStatusUseCaseImpl;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.UserId;
import soat.project.hackaton_soat_manager.domain.video.VideoName;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateVideoStatusUseCaseTest {

    @Mock
    private VideoProcessingGateway videoGateway;

    @Mock
    private NotificationQueueGateway notificationGateway;

    @InjectMocks
    private UpdateVideoStatusUseCaseImpl useCase;

    @Test
    void shouldMarkVideoProcessed() {

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

        var command = UpdateVideoStatusCommand.success(
                processId.getValue(),
                "bucket",
                "file/key"
        );

        var output = useCase.execute(command);

        assertThat(output.status()).isEqualTo("PROCESSED");

        verify(videoGateway).save(video);
        verify(notificationGateway).sendNotification(
                any(), any(), any(), any(), any()
        );
    }
}