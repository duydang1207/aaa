package fit.api.social_network.model.response.post;

import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private UserResponse user;
    private List<String> image_url;
    private String caption;
    private Integer kind;
    private Date createdDate;
    private Date modifiedDate;
    private Integer status;
    private Integer likedAmount = 0;
    private Integer commentedAmount = 0;
    private Boolean isLiked = false;
}
