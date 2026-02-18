package soat.project.hackaton_soat_manager.application.output.video.update;

public record UpdateVideoStatusOutput(
        String processId,
        String status,
        boolean updated
) {

    public static UpdateVideoStatusOutput notUpdated(String processId, String status) {
        return new UpdateVideoStatusOutput(processId, status, false);
    }

    public static UpdateVideoStatusOutput updated(String processId, String status) {
        return new UpdateVideoStatusOutput(processId, status, true);
    }
}