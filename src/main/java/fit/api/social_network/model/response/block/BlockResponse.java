package fit.api.social_network.model.response.block;

import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BlockResponse {
    private Long id;
    private UserResponse user;
    private UserResponse blockUser;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
