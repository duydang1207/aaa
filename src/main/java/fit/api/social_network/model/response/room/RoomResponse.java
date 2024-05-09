package fit.api.social_network.model.response.room;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RoomResponse {
    private Long id;
    private String name;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
