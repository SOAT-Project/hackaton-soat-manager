package soat.project.hackaton_soat_manager.infrastructure.video.presenter;

import soat.project.hackaton_soat_manager.application.output.video.get.GetVideoByProcessIdOutput;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.GetVideoByProcessIdResponse;

public class GetVideoByProcessIdPresenter {

    public static GetVideoByProcessIdResponse present(GetVideoByProcessIdOutput output) {
        return new GetVideoByProcessIdResponse(
                output.processId(),
                output.userId(),
                output.status(),
                output.createdAt(),
                output.processedAt()
        );
    }
}
