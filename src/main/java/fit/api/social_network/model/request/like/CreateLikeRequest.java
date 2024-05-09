package fit.api.social_network.model.request.like;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateLikeRequest implements Serializable {
    @NonNull
    private Long postId;
    private Long commentId;
    @NonNull
    private Integer kind;
}
