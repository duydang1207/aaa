package fit.api.social_network.model.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RegisterRequest implements Serializable {
    @NotEmpty(message = "Name is required!")
    private String name;
    @NotEmpty(message = "Email is required!")
    @Email(message = "Email incorrect format")
    private String email;
    @NotEmpty(message = "Password is required!")
    private String password;
}
