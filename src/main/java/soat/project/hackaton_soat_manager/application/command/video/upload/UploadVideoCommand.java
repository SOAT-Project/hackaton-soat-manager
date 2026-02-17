package soat.project.hackaton_soat_manager.application.command.video.upload;

public record UploadVideoCommand(
        String userId,
        String fileName,
        String contentType,
        byte[] fileContent
) {

    public static UploadVideoCommand with(
            final String userId,
            final String fileName,
            final String contentType,
            final byte[] fileContent
    ) {
        return new UploadVideoCommand(
                userId,
                fileName,
                contentType,
                fileContent
        );
    }
}
