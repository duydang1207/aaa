package fit.api.social_network.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fit.api.social_network.controller.AbasicMethod;
import fit.api.social_network.model.entity.Comments;
import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.RoomMember;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.enums.SOCKET_TYPE;
import fit.api.social_network.model.mapper.CommentMapper;
import fit.api.social_network.model.request.chat.ChatMessageRequest;
import fit.api.social_network.model.request.socket.SocketRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.comment.CommentResponse;
import fit.api.social_network.repository.RoomMemberRepository;
import fit.api.social_network.repository.RoomRepository;
import fit.api.social_network.repository.UserRepository;
import fit.api.social_network.security.JwtService;
import fit.api.social_network.service.SocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class HandlerSocket extends TextWebSocketHandler {
    @Autowired
    UserRepository userRepository;
    @Autowired
    AbasicMethod abasicMethod;
    @Autowired
    SocketService chatService;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    RoomMemberRepository roomMemberRepository;
    @Autowired
    JwtService jwtService;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        chatService.putSession(session);

        UriComponents uriComponents = UriComponentsBuilder.fromUri(session.getUri()).build();
        System.out.println(session.getUri());
        MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();

        Long id = Long.valueOf(queryParams.getFirst("id"));
        // Xử lý token
        if (id != null) {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                user.setSession(session.getId());
                userRepository.save(user);
            }
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        SocketRequest<ChatMessageRequest> socketRequest = objectMapper.readValue(textMessage.getPayload(),SocketRequest.class);
        if(socketRequest.getType() == SOCKET_TYPE.CHAT){
            ChatMessageRequest messageRequest = objectMapper.readValue(objectMapper.writeValueAsString(socketRequest.getData())
                    ,ChatMessageRequest.class);
            handlerChat(session, messageRequest);
        }
        else if(socketRequest.getType() == SOCKET_TYPE.STORY){
            ChatMessageRequest messageRequest = objectMapper.readValue(objectMapper.writeValueAsString(socketRequest.getData())
                    ,ChatMessageRequest.class);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        chatService.removeSession(session);
    }

    private void handlerChat(WebSocketSession session,  ChatMessageRequest chatMessageRequest) throws Exception  {
        User user =  userRepository.findById(chatMessageRequest.getSenderId()).orElse(null);
        if(user!=null){
            Room currentRoom = roomRepository.findById(chatMessageRequest.getRoomId()).orElse(new Room());
            if(chatMessageRequest.getRoomId()==0 || chatMessageRequest.getRoomId()==null){
                chatMessageRequest.getReceivers().add(0,user.getId());
                currentRoom = chatService.newRoom(chatMessageRequest.getReceivers(), chatMessageRequest.getGroupName());
            }

            if(roomMemberRepository.findByRoomAndUser(currentRoom,user)==null)
            {
                ObjectMapper objectMapper = new ObjectMapper();
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.error("User does not exist in room");
                String jsonMessage = objectMapper.writeValueAsString(apiResponse);
                session.sendMessage(new TextMessage(jsonMessage));
                return;
            }

            List<WebSocketSession> sessionReceives = new ArrayList<>();
            for(RoomMember roomMember: roomMemberRepository.findAllByRoom(currentRoom)){
                WebSocketSession socketSession = chatService.getSession(roomMember.getUser().getSession());
                if(socketSession!=null)
                    sessionReceives.add(socketSession);
            }

            chatService.sendMessage(sessionReceives,chatMessageRequest,currentRoom,user);
        }
    }


}
