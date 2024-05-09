package fit.api.social_network.model.request.notification;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangeStateNotification {
    @NotNull(message = "id cant not be null")
    private Long id;
}
