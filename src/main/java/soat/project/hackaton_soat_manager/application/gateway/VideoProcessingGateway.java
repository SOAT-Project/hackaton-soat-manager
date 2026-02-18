package soat.project.hackaton_soat_manager.application.gateway;

import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.UserId;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;

import java.util.List;
import java.util.Optional;

public interface VideoProcessingGateway {

    VideoProcessing save(VideoProcessing video);

    Optional<VideoProcessing> findByProcessId(ProcessId processId);

    List<VideoProcessing> findByUserId(UserId userId);
}
