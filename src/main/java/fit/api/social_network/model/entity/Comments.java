package fit.api.social_network.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comments extends BaseEntity{
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Posts post;
    @ManyToOne
    @JoinColumn(name="parent_id")
    private Comments parent;
    private Integer kind;
    @Column(columnDefinition = "text")
    private String comment;
}
