package soat.project.hackaton_soat_manager.application.command.video.upload;

import soat.project.hackaton_soat_manager.domain.video.VideoName;

public record UploadVideoCommand(
        String userId,
        String fileName,
        VideoName videoname,
        String contentType,
        byte[] fileContent
) {

    public static UploadVideoCommand with(
            final String userId,
            final String fileName,
            final VideoName videoname,
            final String contentType,
            final byte[] fileContent
    ) {
        return new UploadVideoCommand(
                userId,
                fileName,
                videoname,
                contentType,
                fileContent
        );
    }
}
