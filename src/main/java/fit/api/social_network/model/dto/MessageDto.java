package fit.api.social_network.model.dto;

import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
@Getter
@Setter
public class MessageDto {
    private Long id;
    private Room room;
    private User sender;
    private String message;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
    private List<User> users;
}
