package soat.project.hackaton_soat_manager.infrastructure.video.controller;


import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;

public interface VideoController {

    UploadVideoResponse upload(
            String userId,
            String filename,
            String contentType,
            byte[] fileContent
    );
}