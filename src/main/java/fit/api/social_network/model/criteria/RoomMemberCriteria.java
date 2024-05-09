package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.*;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomMemberCriteria {
    private Long id;
    private Integer status;
    private Long roomId;
    private Long userId;

    public Specification<RoomMember> getSpecification() {
        return new Specification<RoomMember>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<RoomMember> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getUserId() != null) {
                    Join<RoomMember, User> joinSeller = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getUserId()));
                }
                if (getRoomId() != null) {
                    Join<RoomMember, Room> joinSeller = root.join("room", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getRoomId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
