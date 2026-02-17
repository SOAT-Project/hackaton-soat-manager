package soat.project.hackaton_soat_manager.infrastructure.web.sqs.model;

public record VideoProcessMessage(
        String process_id,
        String video_bucket,
        String video_key
) {

    public static VideoProcessMessage of(
            String processId,
            String bucket,
            String key
    ) {
        return new VideoProcessMessage(
                processId,
                bucket,
                key
        );
    }
}
