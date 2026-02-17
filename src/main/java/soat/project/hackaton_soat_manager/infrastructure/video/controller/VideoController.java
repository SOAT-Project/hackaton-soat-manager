package soat.project.hackaton_soat_manager.infrastructure.video.controller;


import soat.project.hackaton_soat_manager.infrastructure.video.model.response.DownloadVideoResponse;
import soat.project.hackaton_soat_manager.infrastructure.video.model.response.UploadVideoResponse;

public interface VideoController {

    UploadVideoResponse upload(
            String userId,
            String filename,
            String videoname,
            String contentType,
            byte[] fileContent
    );

    DownloadVideoResponse download(String processId);

}