package fit.api.social_network.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.socket.WebSocketSession;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Room extends BaseEntity{
    private String name;
    Integer type;
    //    message string Sao lai them
}
