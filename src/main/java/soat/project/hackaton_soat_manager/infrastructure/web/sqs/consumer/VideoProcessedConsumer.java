package soat.project.hackaton_soat_manager.infrastructure.web.sqs.consumer;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;
import soat.project.hackaton_soat_manager.application.command.video.update.UpdateVideoStatusCommand;
import soat.project.hackaton_soat_manager.application.usecase.video.update.UpdateVideoStatusUseCase;
import soat.project.hackaton_soat_manager.infrastructure.web.sqs.model.VideoProcessedMessage;

@Component
public class VideoProcessedConsumer {

    private final UpdateVideoStatusUseCase useCase;

    public VideoProcessedConsumer(UpdateVideoStatusUseCase useCase) {
        this.useCase = useCase;
    }

    @SqsListener("${aws.sqs.processed-queue.queue-name}")
    public void listen(VideoProcessedMessage message) {

        UpdateVideoStatusCommand command;

        if (message.error_message() != null) {
            command = UpdateVideoStatusCommand.failure(
                    message.process_id(),
                    message.error_message()
            );
        } else {
            command = UpdateVideoStatusCommand.success(
                    message.process_id(),
                    message.file_bucket(),
                    message.file_key()
            );
        }

        useCase.execute(command);
    }
}
