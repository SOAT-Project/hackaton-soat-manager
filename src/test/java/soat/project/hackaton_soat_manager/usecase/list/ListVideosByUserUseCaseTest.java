package soat.project.hackaton_soat_manager.usecase.list;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import soat.project.hackaton_soat_manager.application.command.video.list.ListVideosByUserCommand;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.list.ListVideosByUserUseCaseImpl;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.UserId;
import soat.project.hackaton_soat_manager.domain.video.VideoName;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListVideosByUserUseCaseTest {

    @Mock
    private VideoProcessingGateway gateway;

    @InjectMocks
    private ListVideosByUserUseCaseImpl useCase;

    @Test
    void shouldListVideos() {

        var userId = UserId.of("user1");

        var video = VideoProcessing.create(
                userId,
                ProcessId.generate(),
                VideoName.of("video"),
                "file.mp4",
                100,
                "bucket",
                "path"
        );

        when(gateway.findByUserId(userId))
                .thenReturn(List.of(video));

        var command = new ListVideosByUserCommand(userId.getValue());

        var output = useCase.execute(command);

        assertThat(output.videos()).hasSize(1);
    }

    @Test
    void shouldReturnEmptyList() {

        var userId = UserId.of("user1");

        when(gateway.findByUserId(userId))
                .thenReturn(List.of());

        var command = new ListVideosByUserCommand(userId.getValue());

        var output = useCase.execute(command);

        assertThat(output.videos()).isEmpty();
    }
}