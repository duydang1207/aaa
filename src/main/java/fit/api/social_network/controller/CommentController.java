package fit.api.social_network.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.CommentCriteria;
import fit.api.social_network.model.entity.*;
import fit.api.social_network.model.mapper.CommentMapper;
import fit.api.social_network.model.request.comment.CreateCommentRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.repository.CommentsRepository;
import fit.api.social_network.repository.LikesRepository;
import fit.api.social_network.repository.PostsRepository;
import fit.api.social_network.repository.UserRepository;
import fit.api.social_network.service.SocketService;
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
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController extends AbasicMethod{
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private CommentMapper commentsMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private LikesRepository likesRepository;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Comments comment = commentsRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new NotFoundException("Comment Not Found");
        }
        apiResponse.ok(commentsMapper.toResponse(comment));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listComments(CommentCriteria commentsCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<Comments> commentPage = commentsRepository.findAll(commentsCriteria.getSpecification(), pageable);
        apiResponse.ok(commentsMapper.toResponseList(commentPage.getContent()), commentPage.getTotalElements(), commentPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Comments comment = commentsRepository.findById(id).orElse(null);
        if (comment == null) {
            throw new NotFoundException("Comment Not Found");
        }
        deleteReplies(id);
        likesRepository.deleteAllByCommentId(id);
        commentsRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    private void deleteReplies(Long commentId) {
        List<Comments> replies = commentsRepository.findAllByParentId(commentId);
        if (!replies.isEmpty()) {
            for (Comments reply : replies) {
                deleteReplies(reply.getId());
                likesRepository.deleteAllByCommentId(reply.getId());
                commentsRepository.deleteById(reply.getId());
            }
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CreateCommentRequest createCommentRequest, BindingResult bindingResult) throws IOException {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            apiResponse.error("User not found");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        Posts post = postsRepository.findById(createCommentRequest.getPostId()).orElse(null);
        if(post == null){
            apiResponse.error("Post not found");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        Comments comment = commentsMapper.createFromCreateRequest(createCommentRequest);
        comment.setUser(user);
        comment.setPost(post);
        if(createCommentRequest.getKind().equals(SocialConstant.COMMENT_KIND_COMMENT)){
            if(createCommentRequest.getCommentId() == null){
                apiResponse.error("Comment kind comment not allow null commentId ");
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
            Comments parent = commentsRepository.findById(createCommentRequest.getCommentId()).orElse(null);
            if(parent == null){
                apiResponse.error("Comment not found");
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
            comment.setParent(parent);
        }
        post.setCommentedAmount(post.getCommentedAmount()+1);
        commentsRepository.save(comment);
        postsRepository.save(post);
        //create notification
        if(comment.getKind().equals(SocialConstant.COMMENT_KIND_POST)){
            createCommentNotification(post.getUser().getId(),comment);
        }else {
            createRepCommentNotification(comment.getParent().getUser().getId(),comment);
        }
        apiResponse.ok("Create success");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}


