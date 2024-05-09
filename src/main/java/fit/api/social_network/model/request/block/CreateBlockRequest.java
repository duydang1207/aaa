package fit.api.social_network.model.request.block;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateBlockRequest implements Serializable {
    @NonNull
    private Long userId;
}
