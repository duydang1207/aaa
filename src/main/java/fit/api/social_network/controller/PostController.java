package fit.api.social_network.controller;

import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.PostCriteria;
import fit.api.social_network.model.criteria.UserCriteria;
import fit.api.social_network.model.entity.Comments;
import fit.api.social_network.model.entity.Likes;
import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.mapper.PostMapper;
import fit.api.social_network.model.mapper.UserMapper;
import fit.api.social_network.model.request.post.CreatePostRequest;
import fit.api.social_network.model.request.post.UpdatePostRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.post.PostResponse;
import fit.api.social_network.model.response.post.StoryResponse;
import fit.api.social_network.repository.CommentsRepository;
import fit.api.social_network.repository.LikesRepository;
import fit.api.social_network.repository.PostsRepository;
import fit.api.social_network.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController extends AbasicMethod{
    @Autowired
    private PostsRepository postsRepository;
    @Autowired
    private final PostMapper postsMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LikesRepository likesRepository;
    @Autowired
    private CommentsRepository commentsRepository;
    @Autowired
    private UserMapper userMapper;
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponse>> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Posts post = postsRepository.findById(id).orElse(null);
        if (post == null) {
            throw new NotFoundException("Post Not Found");
        }
        PostResponse postResponse = postsMapper.toResponse(post);
        setIsLike(postResponse,user.getId());
        apiResponse.ok(postResponse);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    void setIsLike(PostResponse postResponse, Long userId){
        Likes like = likesRepository.findFirstByPostIdAndUserId(postResponse.getId(),userId);
        if(like!=null){
            postResponse.setIsLiked(true);
        }
    }
    @GetMapping("/list-by-user")
    public ResponseEntity<ApiResponse<List<PostResponse>>> listPostByUser(PostCriteria postsCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        List<Posts> test = postsRepository.findAllByUserId(user.getId());
        Page<Posts> postPage = postsRepository.findAll(postsCriteria.getSpecification(), pageable);
        List<Posts> combinedList = new ArrayList<>(test);

        // Loại bỏ các bài post trùng trong postPage
        List<Posts> distinctPosts = postPage.getContent().stream()
                .filter(post -> !test.contains(post))
                .collect(Collectors.toList());

        // Thêm danh sách test vào đầu danh sách distinctPosts
        combinedList.addAll(distinctPosts);

        // Tạo một trang mới từ danh sách kết hợp
        Page<Posts> combinedPage = new PageImpl<>(combinedList, postPage.getPageable(), combinedList.size());

        List<PostResponse> postResponseList = postsMapper.toResponseList(combinedPage.getContent());
        for (PostResponse postResponse: postResponseList){
            setIsLike(postResponse,user.getId());
        }
        apiResponse.ok(postResponseList, postPage.getTotalElements(), postPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PostResponse>>> listPosts(PostCriteria postsCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Page<Posts> postPage = postsRepository.findAll(postsCriteria.getSpecification(), pageable);
        List<PostResponse> postResponseList = postsMapper.toResponseList(postPage.getContent());
        for (PostResponse postResponse: postResponseList){
            setIsLike(postResponse,user.getId());
        }
        apiResponse.ok(postResponseList, postPage.getTotalElements(), postPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/list-story")
    public ResponseEntity<ApiResponse<List<StoryResponse>>> listStory(UserCriteria userCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        userCriteria.setCreatedStoryHour(24);
        userCriteria.setFollowOfUserId(getCurrentUserId());
        Page<User> users = userRepository.findAll(userCriteria.getSpecification(), pageable);
        List<StoryResponse> storyResponses = new ArrayList<>();
        PostCriteria postCriteria = new PostCriteria();
        postCriteria.setCreatedHour(24);
        postCriteria.setKind(2);
        if(userCriteria.getId() == null){
            postCriteria.setUserId(user.getId());
            StoryResponse storyResponse = new StoryResponse();
            storyResponse.setUser(userMapper.toResponse(user));

            List<Posts> posts = postsRepository.findAll(postCriteria.getSpecification());
            List<PostResponse> postResponseList = postsMapper.toResponseList(posts);
            for (PostResponse postResponse: postResponseList){
                setIsLike(postResponse,user.getId());
            }
            storyResponse.setStories(postResponseList);
            storyResponses.add(storyResponse);
        }
        for (User user1: users.getContent()){
            postCriteria.setUserId(user1.getId());
            StoryResponse storyResponse = new StoryResponse();
            storyResponse.setUser(userMapper.toResponse(user1));

            List<Posts> posts = postsRepository.findAll(postCriteria.getSpecification());
            List<PostResponse> postResponseList = postsMapper.toResponseList(posts);
            for (PostResponse postResponse: postResponseList){
                setIsLike(postResponse,user.getId());
            }
            storyResponse.setStories(postResponseList);
            storyResponses.add(storyResponse);
        }
        apiResponse.ok(storyResponses, users.getTotalElements()+1, users.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/my-post")
    public ResponseEntity<ApiResponse<List<PostResponse>>> myPosts(PostCriteria postsCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        postsCriteria.setUserId(user.getId());
        Page<Posts> postPage = postsRepository.findAll(postsCriteria.getSpecification(), pageable);
        List<PostResponse> postResponseList = postsMapper.toResponseList(postPage.getContent());
        for (PostResponse postResponse: postResponseList){
            setIsLike(postResponse,user.getId());
        }
        apiResponse.ok(postResponseList, postPage.getTotalElements(), postPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Posts post = postsRepository.findById(id).orElse(null);
        if (post == null) {
            throw new NotFoundException("Post Not Found");
        }
        List<Likes> likes = likesRepository.findAllByPost(post);
        for (Likes like:likes) {
            likesRepository.delete(like);
        }
        List<Comments> comments = commentsRepository.findAllByPost(post);
        for (Comments c: comments) {
            deleteReplies(c.getId());
            commentsRepository.deleteById(c.getId());
        }
        postsRepository.deleteById(id);
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
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody CreatePostRequest createPostRequest, BindingResult bindingResult) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Posts posts = postsMapper.createFromRequest(createPostRequest);
        posts.setUser(user);
        postsRepository.save(posts);
        apiResponse.ok("Create success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<String>> create(@Valid @RequestBody UpdatePostRequest updatePostRequest, BindingResult bindingResult) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            throw new NotFoundException("User not found");
        }
        Posts posts = postsRepository.findFirstByIdAndUserId(updatePostRequest.getPostId(),user.getId());
        if(user == null){
            throw new NotFoundException("Post not found");
        }
        postsMapper.updateFromUpdateRequest(updatePostRequest,posts);
        postsRepository.save(posts);
        apiResponse.ok("Update success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}








