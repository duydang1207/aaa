package fit.api.social_network.service;

import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.request.chat.ChatMessageRequest;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

public interface SocketService {
    Room newRoom(List<Long> members, String groupName);


    void sendMessage(List<WebSocketSession> sessionReceives,
                     ChatMessageRequest chatMessageRequest, Room room, User sender) throws IOException;

    void handlerNotification(User user, String jsonMessage) throws IOException;

    void putSession(WebSocketSession session);

    void removeSession(WebSocketSession session);

    WebSocketSession getSession(String sessionId);
}
