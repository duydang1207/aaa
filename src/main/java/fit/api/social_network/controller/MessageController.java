package fit.api.social_network.controller;

import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.LikeCriteria;
import fit.api.social_network.model.criteria.MessageCriteria;
import fit.api.social_network.model.entity.Likes;
import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.mapper.LikeMapper;
import fit.api.social_network.model.mapper.MessageMapper;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.repository.LikesRepository;
import fit.api.social_network.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageMapper messageMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Message message = messageRepository.findById(id).orElse(null);
        if (message == null) {
            throw new NotFoundException("Message Not Found");
        }
        apiResponse.ok(messageMapper.toResponse(message));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listMessages(MessageCriteria messageCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<Message> messagePage = messageRepository.findAll(messageCriteria.getSpecification(), pageable);
        apiResponse.ok(messageMapper.toResponseList(messagePage.getContent()), messagePage.getTotalElements(), messagePage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Message message = messageRepository.findById(id).orElse(null);
        if (message == null) {
            throw new NotFoundException("Message Not Found");
        }
        messageRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}





