package soat.project.hackaton_soat_manager.infrastructure.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import soat.project.hackaton_soat_manager.infrastructure.video.controller.VideoController;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.web.rest.api.VideoAPI;

@RestController
public class RestVideoController implements VideoAPI {

    private final VideoController videoController;

    public RestVideoController(VideoController videoController) {
        this.videoController = videoController;
    }

    @Override
    public ResponseEntity<UploadVideoResponse> upload(
            @RequestPart("file") MultipartFile file,
            @RequestParam("userId") String userId
    ) {
        try {

            var response = videoController.upload(
                    userId,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );

            return ResponseEntity.accepted().body(response);

        } catch (Exception e) {
            throw new RuntimeException("Error uploading video", e);
        }
    }
}