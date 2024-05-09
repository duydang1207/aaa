package fit.api.social_network.model.response.roomMember;

import fit.api.social_network.model.response.room.RoomResponse;
import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomMemberResponse {
    private Long id;
    private RoomResponse room;
    private UserResponse user;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
