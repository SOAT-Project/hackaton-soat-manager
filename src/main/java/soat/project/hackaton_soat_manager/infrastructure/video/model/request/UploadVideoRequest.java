package soat.project.hackaton_soat_manager.infrastructure.video.model.request;


public record UploadVideoRequest(
        String userId,
        String filename,
        String contentType,
        byte[] fileContent
) {}
