package fit.api.social_network.controller;

import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.BadRequestException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.exception.ValidationException;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.request.user.ForgotPasswordRequest;
import fit.api.social_network.model.request.user.LoginRequest;
import fit.api.social_network.model.request.user.RegisterRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.repository.UserRepository;
import fit.api.social_network.service.AuthenticationService;
import fit.api.social_network.service.impl.MailService;
import fit.api.social_network.util.StringUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult){
        ApiResponse apiResponse;
        try {
            Map<String,String> result = authenticationService.register(registerRequest,bindingResult);
            apiResponse = new ApiResponse();
            apiResponse.ok(result);
            return ResponseEntity.ok(apiResponse);
        } catch (NotFoundException ex) {
            throw ex; // Rethrow NotFoundException
        } catch (ValidationException ex) {
            apiResponse = new ApiResponse();
            apiResponse.error(ex.getErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (Exception ex) {
            throw new ApplicationException(); // Handle other exceptions
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult){
        ApiResponse apiResponse;
        try {
            Map<String,String> result = authenticationService.login(loginRequest,bindingResult);
            apiResponse = new ApiResponse();
            apiResponse.ok(result);
            return ResponseEntity.ok(apiResponse);
        } catch (ValidationException ex) {
            apiResponse = new ApiResponse();
            apiResponse.error(ex.getErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
        } catch (ApplicationException ex) {
            apiResponse = new ApiResponse();
            apiResponse.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(apiResponse);
        }
    }

    @PostMapping("/create-password")
    public ResponseEntity<ApiResponse> forgot(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, BindingResult bindingResult){
        ApiResponse apiResponse = new ApiResponse();
        try {
            User user = userRepository.findFirstByEmailAndOtp(forgotPasswordRequest.getEmail(),forgotPasswordRequest.getOtp()).orElse(null);
            if(user == null){
                throw new NotFoundException("User not found");
            }
            Date currentDate = new Date();
            if(!user.getOtp().equals(forgotPasswordRequest.getOtp()) || user.getOtpExpiredDate().before(currentDate) ){
                throw new BadRequestException("Otp not match or expired");
            }
            user.setPassword(passwordEncoder.encode(forgotPasswordRequest.getPassword()));
            userRepository.save(user);
            apiResponse.ok("Success");
            return ResponseEntity.ok(apiResponse);
        } catch (ApplicationException ex) {
            apiResponse = new ApiResponse();
            apiResponse.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(apiResponse);
        }
    }
    @GetMapping("/send-otp")
    public ResponseEntity<ApiResponse> forgot(@RequestParam("email") String email){
        ApiResponse apiResponse = new ApiResponse();
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if(user == null){
                apiResponse.error("User not found");
                return ResponseEntity.ok(apiResponse);
            }
            user.setOtp(StringUtils.generateRandomString(6));
            // Đặt thời hạn hết hạn là 5 phút
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 5);
            user.setOtpExpiredDate(calendar.getTime());
            mailService.sendVerificationCode(email, user.getOtp());
            userRepository.save(user);
            apiResponse.ok("Success");
            return ResponseEntity.ok(apiResponse);
        } catch (ApplicationException ex) {
            apiResponse = new ApiResponse();
            apiResponse.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(apiResponse);
        }
    }
}
