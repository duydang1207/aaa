package fit.api.social_network.model.response.notification;

import lombok.Data;

@Data
public class RepCommentNotification {
    private Long notificationId;
    private Long userId;
    private Long postId;
    private Long repCommentId;
    private String userName;
    private Long commentId;
}
