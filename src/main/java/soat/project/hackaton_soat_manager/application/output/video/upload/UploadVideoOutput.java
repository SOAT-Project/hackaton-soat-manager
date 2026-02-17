package soat.project.hackaton_soat_manager.application.output.video.upload;

public record UploadVideoOutput(
        String processId,
        String status
) {

    public static UploadVideoOutput from(
            final String processId,
            final String status
    ) {
        return new UploadVideoOutput(processId, status);
    }
}
