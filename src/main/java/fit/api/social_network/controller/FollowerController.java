package fit.api.social_network.controller;

import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.BadRequestException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.CommentCriteria;
import fit.api.social_network.model.criteria.FollowerCriteria;
import fit.api.social_network.model.entity.*;
import fit.api.social_network.model.mapper.CommentMapper;
import fit.api.social_network.model.mapper.FollowerMapper;
import fit.api.social_network.model.request.follow.CreateFollowRequest;
import fit.api.social_network.model.request.like.CreateLikeRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.follower.FollowerResponse;
import fit.api.social_network.repository.CommentsRepository;
import fit.api.social_network.repository.FollowersRepository;
import fit.api.social_network.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/followers")
@RequiredArgsConstructor
public class FollowerController extends AbasicMethod{
    @Autowired
    private FollowersRepository followersRepository;
    @Autowired
    private FollowerMapper followersMapper;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FollowerResponse>> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Followers follower = followersRepository.findById(id).orElse(null);
        if (follower == null) {
            throw new NotFoundException("Follower Not Found");
        }
        apiResponse.ok(followersMapper.toResponse(follower));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<FollowerResponse>>> listFollowers(FollowerCriteria followersCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<Followers> followerPage = followersRepository.findAll(followersCriteria.getSpecification(), pageable);
        apiResponse.ok(followersMapper.toResponseList(followerPage.getContent()), followerPage.getTotalElements(), followerPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Followers follower = followersRepository.findById(id).orElse(null);
        if (follower == null) {
            throw new NotFoundException("Follower Not Found");
        }
        followersRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody CreateFollowRequest createFollowRequest, BindingResult bindingResult) throws BadRequestException {
        ApiResponse apiResponse = new ApiResponse();
        User followingUser = userRepository.findById(getCurrentUserId()).orElse(null);
        if(followingUser == null){
            throw new NotFoundException("followingUser not found");
        }
        User user = userRepository.findById(createFollowRequest.getUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Followers follow = followersRepository.findFirstByUserIdAndAndFollowingUserId(createFollowRequest.getUserId(),getCurrentUserId());
        if(follow != null){
            throw new BadRequestException("user already follow this user");
        }
        Followers follower = new Followers();
        follower.setUser(user);
        follower.setFollowingUser(followingUser);
        followersRepository.save(follower);
        apiResponse.ok("Create success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete-by-userId/{userId}")
    public ResponseEntity<ApiResponse<String>> deleteByUserId(@PathVariable Long userId) {
        ApiResponse apiResponse = new ApiResponse();
        User followingUser = userRepository.findById(getCurrentUserId()).orElse(null);
        if(followingUser == null){
            throw new NotFoundException("followingUser not found");
        }
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Followers follow = followersRepository.findFirstByUserIdAndAndFollowingUserId(userId,getCurrentUserId());
        if(follow == null){
            throw new BadRequestException("user not follow this user");
        }
        followersRepository.deleteById(follow.getId());
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}



