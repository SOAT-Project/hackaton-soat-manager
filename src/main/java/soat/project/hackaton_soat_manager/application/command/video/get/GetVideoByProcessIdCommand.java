package soat.project.hackaton_soat_manager.application.command.video.get;

public record GetVideoByProcessIdCommand(String processId) {

    public static GetVideoByProcessIdCommand with(String processId) {
        return new GetVideoByProcessIdCommand(processId);
    }
}
