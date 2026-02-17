package soat.project.hackaton_soat_manager.infrastructure.web.rest.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import soat.project.hackaton_soat_manager.infrastructure.video.model.response.DownloadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;

import java.io.IOException;

@Tag(name = "Videos")
@RequestMapping("/videos")
public interface VideoAPI {

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<UploadVideoResponse> upload(
            @RequestPart("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam("videoName") String videoName
    )throws IOException;

    @GetMapping(
            value = "/{processId}/download",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<DownloadVideoResponse> download(
            @PathVariable("processId") String processId
    );
}