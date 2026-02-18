package soat.project.hackaton_soat_manager.application.usecase.video.list;

import soat.project.hackaton_soat_manager.application.command.video.list.ListVideosByUserCommand;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.output.video.list.ListVideosByUserOutput;
import soat.project.hackaton_soat_manager.application.output.video.list.VideoListItem;
import soat.project.hackaton_soat_manager.domain.video.UserId;

public class ListVideosByUserUseCaseImpl extends ListVideosByUserUseCase {

    public final VideoProcessingGateway gateway;

    public ListVideosByUserUseCaseImpl(final VideoProcessingGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public ListVideosByUserOutput execute(ListVideosByUserCommand command
    ) {

        var userId = UserId.of(command.userId());

        var videos = gateway.findByUserId(userId)
                .stream()
                .map(video -> new VideoListItem(
                        video.getId().getValue(),
                        video.getVideoName().getValue(),
                        video.getStatus().name(),
                        video.getCreatedAt(),
                        video.getProcessedAt()
                ))
                .toList();

        return new ListVideosByUserOutput(videos);
    }
}
