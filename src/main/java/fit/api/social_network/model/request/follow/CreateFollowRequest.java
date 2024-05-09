package fit.api.social_network.model.request.follow;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateFollowRequest implements Serializable {
    @NonNull
    private Long userId;
}
