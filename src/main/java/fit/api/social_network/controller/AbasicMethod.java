package fit.api.social_network.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.handler.HandlerSocket;
import fit.api.social_network.model.entity.Comments;
import fit.api.social_network.model.entity.Likes;
import fit.api.social_network.model.entity.Notifications;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.mapper.NotificationMapper;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.notification.*;
import fit.api.social_network.repository.NotificationsRepository;
import fit.api.social_network.repository.UserRepository;
import fit.api.social_network.service.SocketService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
public class AbasicMethod {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    SocketService socketService;
    @Autowired
    NotificationMapper notificationMapper;
    public Long getCurrentUserId(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            if(user == null){
                log.error("user null");
            }
            return user.getId();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
    public void createCommentNotification(Long userId, Comments comment) throws IOException {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NotFoundException("user not found");
        }
        Notifications notifications = createNotification(SocialConstant.NOTIFICATION_KIND_COMMENT_POST,user);
        NewCommentPostNotification newCommentPostNotification = new NewCommentPostNotification();
        newCommentPostNotification.setNotificationId(notifications.getId());
        newCommentPostNotification.setCommentId(comment.getId());
        newCommentPostNotification.setUserId(comment.getUser().getId());
        newCommentPostNotification.setPostId(comment.getPost().getId());
        newCommentPostNotification.setUserName(comment.getUser().getName());
        notifications.setNotification(convertObjectToJson(newCommentPostNotification));
        notificationsRepository.save(notifications);

        ApiResponse<NotificationResponse> apiResponse = new ApiResponse<>();
        apiResponse.ok(notificationMapper.toResponse(notifications));
        String jsonMessage = objectMapper.writeValueAsString(apiResponse);
        socketService.handlerNotification(user,jsonMessage);
    }
    public void createLikePostNotification(Long userId, Likes likes) throws IOException {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NotFoundException("user not found");
        }
        Notifications notifications = createNotification(SocialConstant.NOTIFICATION_KIND_LIKE_POST,user);
        LikePostNotification likePostNotification = new LikePostNotification();
        likePostNotification.setNotificationId(notifications.getId());
        likePostNotification.setUserId(likes.getUser().getId());
        likePostNotification.setPostId(likes.getPost().getId());
        likePostNotification.setUserName(likes.getUser().getName());
        notifications.setNotification(convertObjectToJson(likePostNotification));
        notificationsRepository.save(notifications);

        ApiResponse<NotificationResponse> apiResponse = new ApiResponse<>();
        apiResponse.ok(notificationMapper.toResponse(notifications));
        String jsonMessage = objectMapper.writeValueAsString(apiResponse);
        socketService.handlerNotification(user,jsonMessage);
    }
    public void createLikeCommentNotification(Long userId, Likes likes) throws IOException {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NotFoundException("user not found");
        }
        Notifications notifications = createNotification(SocialConstant.NOTIFICATION_KIND_LIKE_COMMENT,user);
        LikeCommentNotification likeCommentNotification = new LikeCommentNotification();
        likeCommentNotification.setNotificationId(notifications.getId());
        likeCommentNotification.setUserId(likes.getUser().getId());
        likeCommentNotification.setPostId(likes.getPost().getId());
        likeCommentNotification.setUserName(likes.getUser().getName());
        likeCommentNotification.setCommentId(likes.getComment().getId());
        notifications.setNotification(convertObjectToJson(likeCommentNotification));
        notificationsRepository.save(notifications);

        ApiResponse<NotificationResponse> apiResponse = new ApiResponse<>();
        apiResponse.ok(notificationMapper.toResponse(notifications));
        String jsonMessage = objectMapper.writeValueAsString(apiResponse);
        socketService.handlerNotification(user,jsonMessage);
    }
    public void createRepCommentNotification(Long userId, Comments repComment) throws IOException { //userId người sẽ nhận được notification
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new NotFoundException("user not found");
        }
        //repCommentId là comment được trả lời
        Notifications notifications = createNotification(SocialConstant.NOTIFICATION_KIND_REP_COMMENT,user);
        RepCommentNotification repCommentNotification = new RepCommentNotification();
        repCommentNotification.setNotificationId(notifications.getId());
        repCommentNotification.setUserId(repComment.getUser().getId());
        repCommentNotification.setPostId(repComment.getPost().getId());
        repCommentNotification.setUserName(repComment.getUser().getName());
        repCommentNotification.setRepCommentId(repComment.getParent().getId());
        notifications.setNotification(convertObjectToJson(repCommentNotification));
        notificationsRepository.save(notifications);

        ApiResponse<NotificationResponse> apiResponse = new ApiResponse<>();
        apiResponse.ok(notificationMapper.toResponse(notifications));
        String jsonMessage = objectMapper.writeValueAsString(apiResponse);
        socketService.handlerNotification(user,jsonMessage);
    }
    public String convertObjectToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
    public Notifications createNotification(Integer kind, User user){
        Notifications notification = new Notifications();
        notification.setState(SocialConstant.NOTIFICATION_STATE_SENT);
        notification.setKind(kind);
        notification.setUser(user);
        notificationsRepository.save(notification);
        return notification;
    }
}
