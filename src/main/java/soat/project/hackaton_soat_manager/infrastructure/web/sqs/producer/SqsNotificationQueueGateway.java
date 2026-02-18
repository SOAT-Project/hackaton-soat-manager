package soat.project.hackaton_soat_manager.infrastructure.web.sqs.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import soat.project.hackaton_soat_manager.application.gateway.NotificationQueueGateway;
import soat.project.hackaton_soat_manager.infrastructure.web.sqs.model.VideoNotifyMessage;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SqsNotificationQueueGateway implements NotificationQueueGateway {

    private final SqsAsyncClient sqsAsyncClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public SqsNotificationQueueGateway(
            SqsAsyncClient sqsAsyncClient,
            @Value("${aws.sqs.notify-queue.queue-url}") String queueUrl,
            ObjectMapper objectMapper
    ) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.queueUrl = queueUrl;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendNotification(
            String userId,
            String status,
            String errorMessage,
            String videoName,
            String videoId
    ) {
        try {
            var message = VideoNotifyMessage.of(
                    userId,
                    status,
                    errorMessage,
                    videoName,
                    videoId
            );

            var body = objectMapper.writeValueAsString(message);

            var request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .build();

            sqsAsyncClient.sendMessage(request);

        } catch (Exception e) {
            throw new RuntimeException("Error sending NOTIFY message", e);
        }
    }
}
