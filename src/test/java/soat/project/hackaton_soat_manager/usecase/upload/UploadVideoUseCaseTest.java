package soat.project.hackaton_soat_manager.usecase.upload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soat.project.hackaton_soat_manager.application.command.video.upload.UploadVideoCommand;
import soat.project.hackaton_soat_manager.application.gateway.ProcessingQueueGateway;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.upload.UploadVideoUseCaseImpl;
import soat.project.hackaton_soat_manager.domain.exception.DomainException;
import soat.project.hackaton_soat_manager.domain.video.VideoName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class UploadVideoUseCaseTest {

    @Mock
    private VideoProcessingGateway videoGateway;

    @Mock
    private StorageGateway storageGateway;

    @Mock
    private ProcessingQueueGateway queueGateway;

    private UploadVideoUseCaseImpl useCase;

    @BeforeEach
    void setup() {
        useCase = new UploadVideoUseCaseImpl(
                videoGateway,
                storageGateway,
                queueGateway,
                "videos"
        );
    }

    @Test
    void shouldUploadVideoSuccessfully() {

        var command = new UploadVideoCommand(
                "user1",
                "video.mp4",
                VideoName.of("video.mp4"),
                "video/mp4",
                new byte[]{1,2,3}
        );

        var output = useCase.execute(command);

        assertThat(output.status()).isEqualTo("PENDING");

        verify(storageGateway).upload(any(), any(), any(), any());
        verify(queueGateway).sendProcessingMessage(any(), any(), any(), any());
        verify(videoGateway).save(any());
    }

    @Test
    void shouldThrowWhenExtensionInvalid() {

        var command = new UploadVideoCommand(
                "user1",
                "video.txt",   // <- aqui
                VideoName.of("video.txt"),
                "text/plain",
                new byte[]{1}
        );

        assertThrows(DomainException.class,
                () -> useCase.execute(command));
    }
}