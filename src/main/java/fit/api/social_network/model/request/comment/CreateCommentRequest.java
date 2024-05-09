package fit.api.social_network.model.request.comment;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateCommentRequest implements Serializable {
    @NonNull
    private Long postId;
    @NonNull
    private Integer kind;
    private Long commentId;
    @NotEmpty
    private String comment;
}
