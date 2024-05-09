package fit.api.social_network.model.request.user;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UpdateProfileRequest implements Serializable {
    private String name;
    private String email;
    @NotEmpty
    private String password;
    private String newPassword;
    private String bio;
    private String avatar;
}
