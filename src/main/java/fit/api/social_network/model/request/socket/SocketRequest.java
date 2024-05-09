package fit.api.social_network.model.request.socket;

import fit.api.social_network.model.enums.SOCKET_TYPE;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;

@Getter
@Setter
public class SocketRequest<T> {
    SOCKET_TYPE type;
    T data;
}
