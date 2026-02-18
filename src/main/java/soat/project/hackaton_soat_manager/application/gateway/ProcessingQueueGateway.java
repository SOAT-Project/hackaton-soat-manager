package soat.project.hackaton_soat_manager.application.gateway;

public interface ProcessingQueueGateway {

    void sendProcessingMessage(
            String processId,
            String userId,
            String bucket,
            String key
    );
}
