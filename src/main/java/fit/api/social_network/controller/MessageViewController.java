package fit.api.social_network.controller;

import fit.api.social_network.exception.ApplicationException;
import fit.api.social_network.exception.NotFoundException;
import fit.api.social_network.model.criteria.MessageCriteria;
import fit.api.social_network.model.criteria.MessageViewCriteria;
import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.entity.MessageViews;
import fit.api.social_network.model.mapper.MessageMapper;
import fit.api.social_network.model.mapper.MessageViewMapper;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.repository.MessageRepository;
import fit.api.social_network.repository.MessageViewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message-views")
@RequiredArgsConstructor
public class MessageViewController {
    @Autowired
    private MessageViewsRepository messageViewsRepository;
    @Autowired
    private MessageViewMapper messageViewsMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        MessageViews messageViews = messageViewsRepository.findById(id).orElse(null);
        if (messageViews == null) {
            throw new NotFoundException("MessageViews Not Found");
        }
        apiResponse.ok(messageViewsMapper.toResponse(messageViews));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse> listMessageViews(MessageViewCriteria messageViewsCriteria, Pageable pageable) {
        ApiResponse apiResponse = new ApiResponse();
        Page<MessageViews> messageViewsPage = messageViewsRepository.findAll(messageViewsCriteria.getSpecification(), pageable);
        apiResponse.ok(messageViewsMapper.toResponseList(messageViewsPage.getContent()), messageViewsPage.getTotalElements(), messageViewsPage.getTotalPages());
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        ApiResponse apiResponse = new ApiResponse();
        MessageViews messageViews = messageViewsRepository.findById(id).orElse(null);
        if (messageViews == null) {
            throw new NotFoundException("MessageViews Not Found");
        }
        messageViewsRepository.deleteById(id);
        apiResponse.ok("Delete success");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}






