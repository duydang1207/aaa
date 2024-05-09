package fit.api.social_network.validator;

import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.request.user.RegisterRequest;
import fit.api.social_network.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    UserRepository userRepository;
    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterRequest product = (RegisterRequest) target;
        User checkEmail = userRepository.findByEmail(product.getEmail()).orElse(null);
        if (checkEmail != null) {
            errors.rejectValue("email", "error.email", "Email already exist!");
        }
        User checkName = userRepository.findByName(product.getName()).orElse(null);
        if (checkName != null) {
            errors.rejectValue("name", "error.name", "Name already exist!");
        }
    }
}
