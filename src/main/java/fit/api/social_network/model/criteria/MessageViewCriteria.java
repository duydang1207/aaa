package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.entity.MessageViews;
import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.User;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class MessageViewCriteria {
    private Long id;
    private Integer status;
    private Long messageId;
    private Long viewerId;

    public Specification<MessageViews> getSpecification() {
        return new Specification<MessageViews>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<MessageViews> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getViewerId() != null) {
                    Join<MessageViews, User> joinSeller = root.join("viewer", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getViewerId()));
                }
                if (getMessageId() != null) {
                    Join<MessageViews, Message> joinSeller = root.join("message", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getMessageId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
