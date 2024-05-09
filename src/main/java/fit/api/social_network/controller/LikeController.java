package fit.api.social_network.controller;

import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.exception.BadRequestException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.LikeCriteria;
import fit.api.social_network.model.entity.Comments;
import fit.api.social_network.model.entity.Likes;
import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.mapper.LikeMapper;
import fit.api.social_network.model.request.like.CreateLikeRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.repository.CommentsRepository;
import fit.api.social_network.repository.LikesRepository;
import fit.api.social_network.repository.PostsRepository;
import fit.api.social_network.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController extends AbasicMethod {
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private LikeMapper likesMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Likes like = likesRepository.findById(id).orElse(null);
        if (like == null) {
            throw new NotFoundException("Like Not Found");
        }
        apiResponse.ok(likesMapper.toResponse(like));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listLikes(LikeCriteria likesCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<Likes> likePage = likesRepository.findAll(likesCriteria.getSpecification(), pageable);
        apiResponse.ok(likesMapper.toResponseList(likePage.getContent()), likePage.getTotalElements(), likePage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/my-like")
    public ResponseEntity<ApiResponse> listMyLike(LikeCriteria likesCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        likesCriteria.setUserId(getCurrentUserId());
        Page<Likes> likePage = likesRepository.findAll(likesCriteria.getSpecification(), pageable);
        apiResponse.ok(likesMapper.toResponseList(likePage.getContent()), likePage.getTotalElements(), likePage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Likes like = likesRepository.findById(id).orElse(null);
        if (like == null) {
            throw new NotFoundException("Like Not Found");
        }
        likesRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @DeleteMapping("/delete-by-post/{postId}")
    public ResponseEntity<ApiResponse> deleteByCourseId(@PathVariable Long postId) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Posts post = postsRepository.findById(postId).orElse(null);
        if(post == null){
            throw new NotFoundException("Post not found");
        }
        Likes like = likesRepository.findFirstByPostIdAndUserId(postId,user.getId());
        if(like == null){
            throw new BadRequestException("user not like this post");
        }
        likesRepository.deleteAllByPostIdAndUserId(postId,user.getId());
        post.setLikedAmount(postsRepository.getLikeAmount(postId));
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateLikeRequest createLikeRequest, BindingResult bindingResult) throws BadRequestException, IOException {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Likes like;
        if(SocialConstant.LIKE_KIND_POST.equals(createLikeRequest.getKind())){
            like = likesRepository.findFirstByPostIdAndUserIdAndKind(createLikeRequest.getPostId(),user.getId(),SocialConstant.LIKE_KIND_POST);
        }else {
            if(createLikeRequest.getCommentId() == null){
                throw new BadRequestException("create comment like must enter commentId");
            }
            like = likesRepository.findFirstByCommentIdAndUserIdAndKind(createLikeRequest.getCommentId(),user.getId(),SocialConstant.LIKE_KIND_COMMENT);
        }
        if(like != null){
            throw new BadRequestException("user already like this post");
        }
        Posts post = postsRepository.findById(createLikeRequest.getPostId()).orElse(null);
        if(post == null){
            throw new NotFoundException("Post not found");
        }
        like = new Likes();
        like.setUser(user);
        like.setPost(post);
        like.setKind(createLikeRequest.getKind());
        if(SocialConstant.COMMENT_KIND_COMMENT.equals(createLikeRequest.getKind())){
            Comments comments = commentsRepository.findById(createLikeRequest.getCommentId()).orElse(null);
            if(comments == null){
                throw new NotFoundException("Comment not found");
            }
            like.setComment(comments);
        }else {
            post.setLikedAmount(post.getLikedAmount()+1);
        }
        postsRepository.save(post);
        likesRepository.save(like);
        //create notification
        if(like.getKind().equals(SocialConstant.LIKE_KIND_POST)){
            createLikePostNotification(like.getPost().getUser().getId(),like);
        }else {
            createLikeCommentNotification(like.getComment().getUser().getId(),like);
        }
        apiResponse.ok("Create success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}




