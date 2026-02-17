package soat.project.hackaton_soat_manager.infrastructure.persistence.dynamo;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

public record VideoProcessingItem(
        String userId,
        String processId,
        String fileBucket,
        String filePath,
        String status,
        String errorMessage,
        String createdAt,
        String processedAt,
        String fileSize,
        String fileName,
        String videoName
) {

    public Map<String, AttributeValue> toAttributes() {

        Map<String, AttributeValue> map = new HashMap<>();

        map.put("user_id", AttributeValue.builder().s(userId).build());
        map.put("process_id", AttributeValue.builder().s(processId).build());
        map.put("file_bucket", AttributeValue.builder().s(fileBucket).build());
        map.put("file_path", AttributeValue.builder().s(filePath).build());
        map.put("status", AttributeValue.builder().s(status).build());
        map.put("created_at", AttributeValue.builder().s(createdAt).build());
        map.put("file_size", AttributeValue.builder().s(fileSize).build());
        map.put("file_name", AttributeValue.builder().s(fileName).build());
        map.put("video_name", AttributeValue.builder().s(videoName).build());

        if (errorMessage != null) {
            map.put("error_message", AttributeValue.builder().s(errorMessage).build());
        }

        if (processedAt != null) {
            map.put("processed_at", AttributeValue.builder().s(processedAt).build());
        }

        return map;
    }

    public static VideoProcessingItem from(Map<String, AttributeValue> item) {
        return new VideoProcessingItem(
                item.get("user_id").s(),
                item.get("process_id").s(),
                item.get("file_bucket").s(),
                item.get("file_path").s(),
                item.get("status").s(),
                item.containsKey("error_message") ? item.get("error_message").s() : null,
                item.get("created_at").s(),
                item.containsKey("processed_at") ? item.get("processed_at").s() : null,
                item.get("file_size").s(),
                item.get("file_name").s(),
                item.get("video_name").s()
        );
    }
}
