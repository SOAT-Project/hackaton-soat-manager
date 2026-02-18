package soat.project.hackaton_soat_manager.application.command.video.list;

public record ListVideosByUserCommand(String userId) {

    public static ListVideosByUserCommand with(String userId) {
        return new ListVideosByUserCommand(userId);
    }
}

