package soat.project.hackaton_soat_manager.application.gateway;

public interface NotificationQueueGateway {

    void sendNotification(
            String userId,
            String status,
            String errorMessage,
            String videoName,
            String videoId
    );
}
