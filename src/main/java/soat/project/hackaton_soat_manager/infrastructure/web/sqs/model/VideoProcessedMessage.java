package soat.project.hackaton_soat_manager.infrastructure.web.sqs.model;

public record VideoProcessedMessage(
        String process_id,
        String file_bucket,
        String file_key,
        String error_message
) {
}

