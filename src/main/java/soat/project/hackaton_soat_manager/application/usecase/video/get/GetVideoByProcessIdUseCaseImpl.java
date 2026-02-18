package soat.project.hackaton_soat_manager.application.usecase.video.get;

import soat.project.hackaton_soat_manager.application.command.video.get.GetVideoByProcessIdCommand;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.output.video.get.GetVideoByProcessIdOutput;
import soat.project.hackaton_soat_manager.domain.exception.NotFoundException;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

public class GetVideoByProcessIdUseCaseImpl extends GetVideoByProcessIdUseCase {

    private final VideoProcessingGateway gateway;

    public GetVideoByProcessIdUseCaseImpl(final VideoProcessingGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public GetVideoByProcessIdOutput execute(GetVideoByProcessIdCommand command) {

        var processId = ProcessId.of(command.processId());

        var video = gateway.findByProcessId(processId)
                .orElseThrow(() ->
                        NotFoundException.with(VideoProcessing.class, processId)
                );

        return GetVideoByProcessIdOutput.from(video);
    }
}
