package soat.project.hackaton_soat_manager.infrastructure.web.sqs.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import soat.project.hackaton_soat_manager.application.gateway.ProcessingQueueGateway;
import soat.project.hackaton_soat_manager.infrastructure.web.sqs.model.VideoProcessMessage;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


@Component
public class SqsProcessingQueueGateway implements ProcessingQueueGateway {

    private final SqsAsyncClient sqsAsyncClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public SqsProcessingQueueGateway(
            final SqsAsyncClient sqsAsyncClient,
            @Value("${aws.sqs.process-queue.queue-url}") final String queueUrl,
            final ObjectMapper objectMapper
    ) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.queueUrl = queueUrl;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendProcessingMessage(
            String processId,
            String userId,
            String bucket,
            String key
    ) {
        try {
            var message = VideoProcessMessage.of(
                    processId,
                    bucket,
                    key
            );

            var body = objectMapper.writeValueAsString(message);

            var request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(body)
                    .build();

            sqsAsyncClient.sendMessage(request);

        } catch (Exception e) {
            throw new RuntimeException("Error sending PROCESS message", e);
        }
    }
}
