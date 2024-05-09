package fit.api.social_network.model.response.notification;

import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NotificationResponse {
    private Long id;
    private UserResponse user;
    private String notification;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
    private Integer state;
    private Integer kind;
}
