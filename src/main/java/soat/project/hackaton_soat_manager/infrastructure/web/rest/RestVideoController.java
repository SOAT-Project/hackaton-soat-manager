package soat.project.hackaton_soat_manager.infrastructure.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import soat.project.hackaton_soat_manager.infrastructure.video.controller.VideoController;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.DownloadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.web.rest.api.VideoAPI;

import java.io.IOException;

@RestController
public class RestVideoController implements VideoAPI {

    private final VideoController videoController;

    public RestVideoController(VideoController videoController) {
        this.videoController = videoController;
    }

    @Override
    public ResponseEntity<UploadVideoResponse> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam("videoName") String videoName
    )throws IOException {
            var response = videoController.upload(
                    userId,
                    file.getOriginalFilename(),
                    videoName,
                    file.getContentType(),
                    file.getBytes()
            );

            return ResponseEntity.accepted().body(response);
    }

    @Override
    public ResponseEntity<DownloadVideoResponse> download(String processId) {
        return ResponseEntity.ok(videoController.download(processId));
    }

}