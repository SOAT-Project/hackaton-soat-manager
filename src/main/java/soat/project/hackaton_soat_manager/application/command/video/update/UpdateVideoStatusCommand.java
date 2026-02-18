package soat.project.hackaton_soat_manager.application.command.video.update;

public record UpdateVideoStatusCommand(
        String processId,
        String fileBucket,
        String fileKey,
        String errorMessage
) {

    public boolean isFailure() {
        return errorMessage != null && !errorMessage.isBlank();
    }

    public static UpdateVideoStatusCommand success(
            String processId,
            String fileBucket,
            String fileKey
    ) {
        return new UpdateVideoStatusCommand(
                processId,
                fileBucket,
                fileKey,
                null
        );
    }

    public static UpdateVideoStatusCommand failure(
            String processId,
            String errorMessage
    ) {
        return new UpdateVideoStatusCommand(
                processId,
                null,
                null,
                errorMessage
        );
    }
}