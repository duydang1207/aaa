package fit.api.social_network.controller;

import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.NotificationCriteria;
import fit.api.social_network.model.entity.Notifications;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.mapper.NotificationMapper;
import fit.api.social_network.model.request.notification.ChangeStateNotification;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.notification.NotificationResponse;
import fit.api.social_network.repository.NotificationsRepository;
import fit.api.social_network.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController extends AbasicMethod{
    @Autowired
    private NotificationsRepository notificationsRepository;
    @Autowired
    private NotificationMapper notificationsMapper;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Notifications notification = notificationsRepository.findById(id).orElse(null);
        if (notification == null) {
            throw new NotFoundException("Notification Not Found");
        }
        apiResponse.ok(notificationsMapper.toResponse(notification));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listNotifications(NotificationCriteria notificationsCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<Notifications> notificationPage = notificationsRepository.findAll(notificationsCriteria.getSpecification(), pageable);
        apiResponse.ok(notificationsMapper.toResponseList(notificationPage.getContent()), notificationPage.getTotalElements(), notificationPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @GetMapping("/my-notification")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> myNotification(NotificationCriteria notificationsCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        User user = userRepository.findById(getCurrentUserId()).orElse(null);
        if(user == null){
            apiResponse.error("User not found");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
        notificationsCriteria.setUserId(user.getId());
        Page<Notifications> notificationPage = notificationsRepository.findAll(notificationsCriteria.getSpecification(), pageable);
        apiResponse.ok(notificationsMapper.toResponseList(notificationPage.getContent()), notificationPage.getTotalElements(), notificationPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Notifications notification = notificationsRepository.findById(id).orElse(null);
        if (notification == null) {
            throw new NotFoundException("Notification Not Found");
        }
        notificationsRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PutMapping(value = "/change-state", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> changeState(@Valid @RequestBody ChangeStateNotification changeStateNotification, BindingResult bindingResult) {
        ApiResponse apiResponse = new ApiResponse();
        Notifications notification = notificationsRepository.findById(changeStateNotification.getId()).orElse(null);
        if (notification == null)
        {
            throw new NotFoundException("Notification not found");
        }
        if (notification.getState().equals(SocialConstant.NOTIFICATION_STATE_SENT))
        {
            notification.setState(SocialConstant.NOTIFICATION_STATE_READ);
        }
        notificationsRepository.save(notification);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    @PutMapping(value = "/read-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> readAll() {
        Long userId = getCurrentUserId();
        List<Notifications> notifications = notificationsRepository.findAllByUserIdAndState(userId,SocialConstant.NOTIFICATION_STATE_SENT);
        notifications.forEach(noti -> noti.setState(SocialConstant.NOTIFICATION_STATE_READ));
        notificationsRepository.saveAll(notifications);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.ok("read all success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete-all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> deleteAll() {
        ApiResponse apiResponse = new ApiResponse();
        Long userId = getCurrentUserId();
        notificationsRepository.deleteAllByUserId(userId);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}







