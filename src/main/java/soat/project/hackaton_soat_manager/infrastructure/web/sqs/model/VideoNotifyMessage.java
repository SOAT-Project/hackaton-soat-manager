package soat.project.hackaton_soat_manager.infrastructure.web.sqs.model;

public record VideoNotifyMessage(
        String user_id,
        String status,
        String error_message,
        String video_name,
        String video_id
) {

    public static VideoNotifyMessage of(
            String userId,
            String status,
            String errorMessage,
            String videoName,
            String videoId
    ) {
        return new VideoNotifyMessage(
                userId,
                status,
                errorMessage,
                videoName,
                videoId
        );
    }
}
