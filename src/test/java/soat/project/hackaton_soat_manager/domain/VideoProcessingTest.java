package soat.project.hackaton_soat_manager.domain;

import org.junit.jupiter.api.Test;
import soat.project.hackaton_soat_manager.domain.exception.NotificationException;
import soat.project.hackaton_soat_manager.domain.video.*;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class VideoProcessingTest {

    @Test
    void givenValidParams_whenCreateVideoProcessing_thenInstantiateAggregate() {

        final var userId = UserId.of("user-123");
        final var processId = ProcessId.generate();
        final var videoName = VideoName.of("video.mp4");

        final var fileName = "video.mp4";
        final var fileSize = 100L;
        final var bucket = "raw-videos";
        final var path = "uploads/video.mp4";

        final var video = VideoProcessing.create(
                userId,
                processId,
                videoName,
                fileName,
                fileSize,
                bucket,
                path
        );

        assertNotNull(video);
        assertEquals(processId, video.getId());
        assertEquals(userId, video.getUserId());
        assertEquals(videoName, video.getVideoName());
        assertEquals(VideoStatus.PENDING, video.getStatus());

        assertEquals(fileName, video.getFileName());
        assertEquals(fileSize, video.getFileSize());

        assertEquals(bucket, video.getFileBucket());
        assertEquals(path, video.getFilePath());

        assertNull(video.getErrorMessage());
        assertNull(video.getProcessedAt());

        assertNotNull(video.getCreatedAt());
    }

    @Test
    void givenPendingVideo_whenMarkProcessed_thenUpdateStatus() {

        final var video = createPendingVideo();

        final var bucket = "processed";
        final var path = "processed/video.mp4";

        video.markProcessed(bucket, path);

        assertEquals(VideoStatus.PROCESSED, video.getStatus());
        assertEquals(bucket, video.getFileBucket());
        assertEquals(path, video.getFilePath());
        assertNotNull(video.getProcessedAt());
    }

    @Test
    void givenProcessedVideo_whenMarkProcessedAgain_thenIgnore() {

        final var video = createPendingVideo();

        video.markProcessed("bucket", "path");

        final var processedAt = video.getProcessedAt();

        video.markProcessed("other", "other");

        assertEquals(VideoStatus.PROCESSED, video.getStatus());
        assertEquals(processedAt, video.getProcessedAt());
    }

    @Test
    void givenFailureVideo_whenMarkProcessed_thenIgnore() {

        final var video = createPendingVideo();

        video.markFailure("error");

        video.markProcessed("bucket", "path");

        assertEquals(VideoStatus.FAILURE, video.getStatus());
    }

    @Test
    void givenPendingVideo_whenMarkFailure_thenUpdateStatus() {

        final var video = createPendingVideo();

        video.markFailure("processing error");

        assertEquals(VideoStatus.FAILURE, video.getStatus());
        assertEquals("processing error", video.getErrorMessage());
        assertNotNull(video.getProcessedAt());
    }

    @Test
    void givenFailureVideo_whenMarkFailureAgain_thenIgnore() {

        final var video = createPendingVideo();

        video.markFailure("error");

        final var processedAt = video.getProcessedAt();

        video.markFailure("another");

        assertEquals(VideoStatus.FAILURE, video.getStatus());
        assertEquals(processedAt, video.getProcessedAt());
    }

    @Test
    void givenProcessedVideo_whenMarkFailure_thenIgnore() {

        final var video = createPendingVideo();

        video.markProcessed("bucket", "path");

        video.markFailure("error");

        assertEquals(VideoStatus.PROCESSED, video.getStatus());
    }

    @Test
    void givenProcessedVideo_whenCheckFinished_thenReturnTrue() {

        final var video = createPendingVideo();

        video.markProcessed("bucket", "path");

        assertTrue(video.isFinished());
    }

    @Test
    void givenFailureVideo_whenCheckFinished_thenReturnTrue() {

        final var video = createPendingVideo();

        video.markFailure("error");

        assertTrue(video.isFinished());
    }

    @Test
    void givenPendingVideo_whenCheckFinished_thenReturnFalse() {

        final var video = createPendingVideo();

        assertFalse(video.isFinished());
    }

    @Test
    void givenValidParams_whenRehydrate_thenInstantiateAggregate() {

        final var processId = ProcessId.generate();
        final var userId = UserId.of("user");
        final var videoName = VideoName.of("video.mp4");

        final var createdAt = Instant.now();
        final var processedAt = Instant.now();

        final var video = VideoProcessing.rehydrate(
                processId,
                userId,
                videoName,
                VideoStatus.PROCESSED,
                "bucket",
                "path",
                null,
                createdAt,
                processedAt,
                "video.mp4",
                100
        );

        assertNotNull(video);
        assertEquals(processId, video.getId());
        assertEquals(VideoStatus.PROCESSED, video.getStatus());
        assertEquals(processedAt, video.getProcessedAt());
    }

    @Test
    void givenNullUserId_whenRehydrate_thenThrowException() {

        final var processId = ProcessId.generate();
        final var videoName = VideoName.of("video.mp4");

        final var exception = assertThrows(
                NotificationException.class,
                () -> VideoProcessing.rehydrate(
                        processId,
                        null,
                        videoName,
                        VideoStatus.PENDING,
                        "bucket",
                        "path",
                        null,
                        Instant.now(),
                        null,
                        "video.mp4",
                        100
                )
        );

        assertEquals(
                "'userId' must not be null",
                exception.getErrors().getFirst().message()
        );
    }

    private VideoProcessing createPendingVideo() {
        return VideoProcessing.create(
                UserId.of("user"),
                ProcessId.generate(),
                VideoName.of("video.mp4"),
                "video.mp4",
                100,
                "raw",
                "path"
        );
    }
}