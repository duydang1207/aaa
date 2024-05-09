package fit.api.social_network.model.response.messageView;

import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.enums.ROLES;
import fit.api.social_network.model.response.message.MessageResponse;
import fit.api.social_network.model.response.user.UserResponse;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MessageViewResponse {
    private Long id;
    private MessageResponse message;
    private UserResponse viewer;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
