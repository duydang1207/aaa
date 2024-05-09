package fit.api.social_network.service.impl;

import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.ValidationException;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.mapper.RegisterMapper;
import fit.api.social_network.model.request.user.LoginRequest;
import fit.api.social_network.model.request.user.RegisterRequest;
import fit.api.social_network.repository.UserRepository;
import fit.api.social_network.security.JwtService;
import fit.api.social_network.service.AuthenticationService;
import fit.api.social_network.util.ValidatorUtil;
import fit.api.social_network.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserValidator userValidator;
    @Autowired
    ValidatorUtil validatorUtil;
    private final RegisterMapper registerMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    @Override
    public Map<String, String> register(RegisterRequest registerRequest, BindingResult bindingResult){
        try {
            userValidator.validate(registerRequest,bindingResult);

            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = validatorUtil.toErrors(bindingResult.getFieldErrors());
                throw new ValidationException(validationErrors);
            }
            User user = registerMapper.toEntity(registerRequest);
            user.setKind(SocialConstant.USER_KIND_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            String token =jwtService.generateToken(user.getId(),user.getKind(),user);
            Map<String, String> result = new HashMap<>();
            result.put("token",token);
            result.put("userId",user.getId().toString());
            result.put("status",user.getStatus().toString());
            return result;
        }
        catch (ApplicationException ex){
            throw ex;
        }
    }

    @Override
    public Map<String, String> login(LoginRequest loginRequest, BindingResult bindingResult){
        try {
            if (bindingResult.hasErrors()) {
                Map<String, String> validationErrors = validatorUtil.toErrors(bindingResult.getFieldErrors());
                throw new ValidationException(validationErrors);
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            User user = userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
            String token =jwtService.generateToken(user.getId(),user.getKind(),user);
            Map<String, String> result = new HashMap<>();
            result.put("token",token);
            result.put("userId",user.getId().toString());
            result.put("status",user.getStatus().toString());
            return result;
        }
        catch (Exception ex){
            throw new ApplicationException("Username or password incorrect");
        }
    }
}
