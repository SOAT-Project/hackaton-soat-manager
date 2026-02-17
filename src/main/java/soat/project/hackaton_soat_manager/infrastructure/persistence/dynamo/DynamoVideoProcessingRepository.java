package soat.project.hackaton_soat_manager.infrastructure.persistence.dynamo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import soat.project.hackaton_soat_manager.application.gateway.VideoProcessingGateway;
import soat.project.hackaton_soat_manager.domain.video.ProcessId;
import soat.project.hackaton_soat_manager.domain.video.UserId;
import soat.project.hackaton_soat_manager.domain.video.VideoProcessing;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DynamoVideoProcessingRepository implements VideoProcessingGateway {

    private final DynamoDbClient dynamoDbClient;

    public DynamoVideoProcessingRepository(
            final DynamoDbClient dynamoDbClient
    ) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    @Override
    public Optional<VideoProcessing> findByProcessId(ProcessId processId) {

        Map<String, AttributeValue> key = Map.of(
                "process_id", AttributeValue.builder()
                        .s(processId.getValue())
                        .build()
        );

        var response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build());

        if (!response.hasItem() || response.item().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                VideoProcessingMapper.toDomain(
                        VideoProcessingItem.from(response.item())
                )
        );
    }


    @Override
    public VideoProcessing save(VideoProcessing video) {

        final var item = VideoProcessingMapper.toItem(video);

        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item.toAttributes())
                .build());

        return video;
    }


    @Override
    public Optional<VideoProcessing> findByUserIdAndProcessId(
            UserId userId,
            ProcessId processId
    ) {

        Map<String, AttributeValue> key = Map.of(
                "user_id", AttributeValue.builder()
                        .s(userId.getValue())
                        .build(),
                "process_id", AttributeValue.builder()
                        .s(processId.getValue())
                        .build()
        );

        var response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build());

        if (!response.hasItem() || response.item().isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
                VideoProcessingMapper.toDomain(
                        VideoProcessingItem.from(response.item())
                )
        );
    }


    @Override
    public List<VideoProcessing> findAllByUserId(UserId userId) {

        var response = dynamoDbClient.query(QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression("user_id = :uid")
                .expressionAttributeValues(Map.of(
                        ":uid", AttributeValue.builder()
                                .s(userId.getValue())
                                .build()
                ))
                .build());

        return response.items().stream()
                .map(VideoProcessingItem::from)
                .map(VideoProcessingMapper::toDomain)
                .toList();
    }
}
