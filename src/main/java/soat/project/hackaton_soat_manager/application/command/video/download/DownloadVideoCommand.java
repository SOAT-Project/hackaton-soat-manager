package soat.project.hackaton_soat_manager.application.command.video.download;

public record DownloadVideoCommand(
        String processId
) {

    public static DownloadVideoCommand with(String processId) {
        return new DownloadVideoCommand(processId);
    }
}
