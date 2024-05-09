package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Block;
import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.enums.ROLES;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class MessageCriteria {
    private Long id;
    private Integer status;
    private Long roomId;
    private Long senderId;
    private String message;


    public Specification<Message> getSpecification() {
        return new Specification<Message>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(!StringUtils.isBlank(getMessage())){
                    predicates.add(cb.equal(cb.lower(root.get("message")),"%"+ getMessage()+"%"));
                }
                if (getSenderId() != null) {
                    Join<Message, User> joinSeller = root.join("sender", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getSenderId()));
                }
                if (getRoomId() != null) {
                    Join<Message, Room> joinSeller = root.join("room", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getRoomId()));
                }
                query.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
