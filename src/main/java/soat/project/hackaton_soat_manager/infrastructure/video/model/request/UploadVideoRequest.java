package soat.project.hackaton_soat_manager.infrastructure.video.model.request;


public record UploadVideoRequest(
        String originalFilename,
        String contentType,
        long size
) {}
