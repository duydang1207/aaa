package fit.api.social_network.model.response.post;

import fit.api.social_network.model.response.user.UserResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class StoryResponse {
    private UserResponse user;
    private List<PostResponse> stories;
}
