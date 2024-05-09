package fit.api.social_network.model.response.message;

import fit.api.social_network.model.response.room.RoomResponse;
import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessageResponse {
    private Long id;
    private RoomResponse room;
    private UserResponse sender;
    private String message;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
