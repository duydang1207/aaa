package fit.api.social_network.model.response.like;

import fit.api.social_network.model.response.comment.CommentResponse;
import fit.api.social_network.model.response.post.PostResponse;
import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class LikeResponse {
    private Long id;
    private UserResponse user;
    private PostResponse post;
    private CommentResponse comment;
    private Integer kind;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
}
