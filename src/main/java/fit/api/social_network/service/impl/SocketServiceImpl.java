package fit.api.social_network.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fit.api.social_network.constant.SocialConstant;
import fit.api.social_network.model.entity.*;
import fit.api.social_network.model.mapper.MessageMapper;
import fit.api.social_network.model.request.chat.ChatMessageRequest;
import fit.api.social_network.model.response.ApiResponse;
import fit.api.social_network.model.response.message.MessageResponse;
import fit.api.social_network.repository.MessageRepository;
import fit.api.social_network.repository.RoomMemberRepository;
import fit.api.social_network.repository.RoomRepository;
import fit.api.social_network.repository.UserRepository;
import fit.api.social_network.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SocketServiceImpl implements SocketService {
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    RoomMemberRepository roomMemberRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageMapper messageMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public Room newRoom(List<Long> members, String groupName) {
        Room room = new Room();
        if (members.size() > 2) {
            room.setType(SocialConstant.ROOM_TYPE_GROUP);
            room.setName(groupName);
        } else room.setType(SocialConstant.ROOM_TYPE_PERSONAL);
        Room newRoom = roomRepository.save(room);
        addUserToRoom(room, members);
        return newRoom;
    }

    public void addUserToRoom(Room room, List<Long> members) {
        for (Long member : members) {
            User user = userRepository.findById(member).orElse(null);
            if (user == null)
                continue;
            RoomMember roomMember = new RoomMember();
            roomMember.setRoom(room);
            roomMember.setUser(user);
            roomMemberRepository.save(roomMember);
        }
    }

    @Override
    public void sendMessage(List<WebSocketSession> sessionReceives,
                            ChatMessageRequest chatMessageRequest, Room room, User sender) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Message message = new Message();
        message.setMessage(chatMessageRequest.getMessage());
        message.setRoom(room);
        message.setSender(sender);
        messageRepository.save(message);

        ApiResponse<MessageResponse> apiResponse = new ApiResponse<>();
        apiResponse.ok(messageMapper.toResponse(message));
        String jsonMessage = objectMapper.writeValueAsString(apiResponse);
        for (WebSocketSession session : sessionReceives) {
            session.sendMessage(new TextMessage(jsonMessage));
        }
        room.setModifiedDate(new Date());
        roomRepository.save(room);

    }
    @Override
    public void handlerNotification(User user, String jsonMessage) throws IOException {
        if(user.getSession()!=null) {
            WebSocketSession socketSession = sessions.get(user.getSession());
            if (socketSession != null) {
                socketSession.sendMessage(new TextMessage(jsonMessage));
            }
        }
    }
    @Override
    public void putSession(WebSocketSession session){
        sessions.put(session.getId(),session);
    }
    @Override
    public void removeSession(WebSocketSession session){
        sessions.remove(session);
    }
    @Override
    public WebSocketSession getSession(String sessionId){
        if(sessionId==null)
            return null;
        return sessions.get(sessionId);
    }
}
