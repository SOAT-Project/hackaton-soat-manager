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
    @Value("${aws.dynamodb.table-name}")
    private String tableName;

    public DynamoVideoProcessingRepository(
            final DynamoDbClient dynamoDbClient
    ) {
        this.dynamoDbClient = dynamoDbClient;
    }



    @Override
    public Optional<VideoProcessing> findByProcessId(ProcessId processId) {

        var response = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(Map.of(
                        "process_id", AttributeValue.builder()
                                .s(processId.getValue())
                                .build()
                ))
                .build());

        if (!response.hasItem()) {
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
    public List<VideoProcessing> findByUserId(UserId userId) {

        System.out.println("USER_ID QUERY: >" + userId.getValue() + "<");
        System.out.println("TABLE NAME: " + tableName);



        var response = dynamoDbClient.query(QueryRequest.builder()
                .tableName(tableName)
                .indexName("user_id-index")
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
