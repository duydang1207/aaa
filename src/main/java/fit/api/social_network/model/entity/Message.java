package fit.api.social_network.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Message extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="room_id", nullable = false)
    private Room room;
    @ManyToOne
    @Cascade(CascadeType.REMOVE)
    @JoinColumn(name="sender_id", nullable = false)
    private User sender;
    @Column(columnDefinition = "text")
    private String message;
}
