package soat.project.hackaton_soat_manager.infrastructure.video.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import soat.project.hackaton_soat_manager.application.gateway.NotificationQueueGateway;
import soat.project.hackaton_soat_manager.application.gateway.ProcessingQueueGateway;
import soat.project.hackaton_soat_manager.application.gateway.StorageGateway;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.application.usecase.video.download.DownloadVideoUseCase;
import soat.project.hackaton_soat_manager.application.usecase.video.download.DownloadVideoUseCaseImpl;
import soat.project.hackaton_soat_manager.application.usecase.video.update.UpdateVideoStatusUseCase;
import soat.project.hackaton_soat_manager.application.usecase.video.update.UpdateVideoStatusUseCaseImpl;
import soat.project.hackaton_soat_manager.application.usecase.video.upload.UploadVideoUseCase;
import soat.project.hackaton_soat_manager.application.usecase.video.upload.UploadVideoUseCaseImpl;

@Configuration
public class VideoUseCaseConfig {

    @Bean
    public UploadVideoUseCase uploadVideoUseCase(
            VideoProcessingGateway videoProcessingGateway,
            StorageGateway storageGateway,
            ProcessingQueueGateway processingQueueGateway,
            @Value("${aws.s3.bucket}") String bucket
    ){
        return new UploadVideoUseCaseImpl(videoProcessingGateway, storageGateway, processingQueueGateway, bucket);
    }

    @Bean
    public UpdateVideoStatusUseCase updateVideoStatusUseCase(
            VideoProcessingGateway videoProcessingGateway,
            NotificationQueueGateway notificationQueueGateway
    ){
        return new UpdateVideoStatusUseCaseImpl(videoProcessingGateway, notificationQueueGateway);
    }

    @Bean
    public DownloadVideoUseCase downloadVideoUseCase(
            VideoProcessingGateway videoProcessingGateway,
            StorageGateway storageGateway
    ){
        return new DownloadVideoUseCaseImpl(videoProcessingGateway, storageGateway);
    }

}
