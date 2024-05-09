package fit.api.social_network.model.response.notification;

import lombok.Data;

@Data
public class LikePostNotification {
    private Long notificationId;
    private Long userId;
    private Long postId;
    private String userName;
}
