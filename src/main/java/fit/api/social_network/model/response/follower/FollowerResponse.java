package fit.api.social_network.model.response.follower;

import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FollowerResponse {
    private Long id;
    private UserResponse user;
    private UserResponse followingUser;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
