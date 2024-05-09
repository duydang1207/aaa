package fit.api.social_network.model.request.chat;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatMessageRequest implements Serializable {
    Long roomId;
    String message;
    String groupName;
    Long postId;
    List<Long> receivers; // Chua co room ms can
    Long senderId;
}
