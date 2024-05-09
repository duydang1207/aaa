package fit.api.social_network.model.response.comment;

import fit.api.social_network.model.response.post.PostResponse;
import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentResponse {
    private Long id;
    private UserResponse user;
    private PostResponse post;
    private CommentResponse parent;
    private Integer kind;
    private String comment;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
