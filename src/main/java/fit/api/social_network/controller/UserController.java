package fit.api.social_network.controller;

import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.BadRequestException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.UserCriteria;
import fit.api.social_network.model.entity.Followers;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.mapper.UserMapper;
import fit.api.social_network.model.request.follow.CreateFollowRequest;
import fit.api.social_network.model.request.user.UpdateProfileRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.user.GeneralInfoDto;
import fit.api.social_network.model.response.user.GeneralInfoDtoImpl;
import fit.api.social_network.model.response.user.UserResponse;
import fit.api.social_network.repository.FollowersRepository;
import fit.api.social_network.repository.LikesRepository;
import fit.api.social_network.repository.NotificationsRepository;
import fit.api.social_network.repository.UserRepository;
import fit.api.social_network.service.impl.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController extends AbasicMethod{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FollowersRepository followersRepository;
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private LikesRepository likesRepository;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id){
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            throw new NotFoundException("User Not Found");
        }
        UserResponse userResponse = userMapper.toResponse(user);
        setIsFollowed(userResponse);
        apiResponse.ok(userResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(){
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user==null){
            throw new NotFoundException("User Not Found");
        }
        UserResponse userResponse = userMapper.toResponse(user);
        setIsFollowed(userResponse);
        apiResponse.ok(userResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/general-info/{userId}")
    public ResponseEntity<ApiResponse> getGeneralInfo(@PathVariable Long userId){
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(userId).orElse(null);
        if(user==null){
            throw new NotFoundException("User Not Found");
        }
        GeneralInfoDto generalInfoDto = userRepository.getGeneralInfo(userId);
        apiResponse.ok(generalInfoDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    void setIsFollowed(UserResponse userResponse){
        Followers followers = followersRepository.findFirstByUserIdAndAndFollowingUserId(userResponse.getId(),getCurrentUserId());
        if(followers != null){
            userResponse.setIsFollowed(true);
        }
    }
    @GetMapping("/list")
    public ResponseEntity<ApiResponse> getById(UserCriteria userCriteria, Pageable pageable){
        ApiResponse apiResponse = new ApiResponse();
        Page<User> userPage = userRepository.findAll(userCriteria.getSpecification(),pageable);
        List<UserResponse> userResponses = userMapper.toResponseList(userPage.getContent());
        for(UserResponse userResponse: userResponses){
            setIsFollowed(userResponse);
        }
        apiResponse.ok(userResponses,userPage.getTotalElements(),userPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id){
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(id).orElse(null);
        if(user==null){
            throw new NotFoundException("User Not Found");
        }
        notificationsRepository.deleteAllByUserId(id);
        likesRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<String>> update(@Valid @RequestBody UpdateProfileRequest updateProfileRequest, BindingResult bindingResult) throws BadRequestException {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        if(!passwordEncoder.matches(updateProfileRequest.getPassword(),user.getPassword())){
            throw new BadRequestException("Password not correct");
        }
        userMapper.updateProfile(updateProfileRequest,user);
        if(StringUtils.isNotBlank(updateProfileRequest.getNewPassword())){
            user.setPassword(passwordEncoder.encode(updateProfileRequest.getNewPassword()));
        }
        userRepository.save(user);
        apiResponse.ok("Update success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
