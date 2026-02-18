package soat.project.hackaton_soat_manager.infrastructure.persistence.dynamo;

import soat.project.hackaton_soat_manager.domain.video.*;

import java.time.Instant;

public class VideoProcessingMapper {

    public static VideoProcessingItem toItem(VideoProcessing video) {

        return new VideoProcessingItem(
                video.getUserId().getValue(),
                video.getId().getValue(),
                video.getFileBucket(),
                video.getFilePath(),
                video.getStatus().name(),
                video.getErrorMessage(),
                video.getCreatedAt().toString(),
                video.getProcessedAt() != null ? video.getProcessedAt().toString() : null,
                String.valueOf(video.getFileSize()),
                video.getFileName(),
                video.getVideoName().getValue()
        );
    }

    public static VideoProcessing toDomain(VideoProcessingItem item) {

        return VideoProcessing.rehydrate(
                ProcessId.of(item.processId()),
                UserId.of(item.userId()),
                VideoName.of(item.videoName()),
                VideoStatus.valueOf(item.status()),
                item.fileBucket(),
                item.filePath(),
                item.errorMessage(),
                Instant.parse(item.createdAt()),
                item.processedAt() != null ? Instant.parse(item.processedAt()) : null,
                item.fileName(),
                Long.parseLong(item.fileSize())
        );
    }
}
