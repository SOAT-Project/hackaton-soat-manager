package soat.project.hackaton_soat_manager.usecase.get;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soat.project.hackaton_soat_manager.application.command.video.get.GetVideoByProcessIdCommand;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.get.GetVideoByProcessIdUseCaseImpl;
import soat.project.hackaton_soat_manager.domain.exception.NotFoundException;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.UserId;
import soat.project.hackaton_soat_manager.domain.video.VideoName;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetVideoByProcessIdUseCaseTest {

    @Mock
    private VideoProcessingGateway gateway;

    @InjectMocks
    private GetVideoByProcessIdUseCaseImpl useCase;

    @Test
    void shouldReturnVideo() {

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

        when(gateway.findByProcessId(processId))
                .thenReturn(Optional.of(video));

        var command = new GetVideoByProcessIdCommand(processId.getValue());

        var output = useCase.execute(command);

        assertThat(output.processId()).isEqualTo(processId.getValue());
    }

    @Test
    void shouldThrowWhenVideoNotFound() {

        var processId = ProcessId.generate();

        when(gateway.findByProcessId(processId))
                .thenReturn(Optional.empty());

        var command = new GetVideoByProcessIdCommand(processId.getValue());

        assertThrows(NotFoundException.class,
                () -> useCase.execute(command));
    }
}