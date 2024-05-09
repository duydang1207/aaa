package fit.api.social_network.model.response.notification;

import lombok.Data;

@Data
public class NewCommentPostNotification {
    private Long notificationId;
    private Long userId;
    private Long postId;
    private String userName;
    private Long commentId;
}
