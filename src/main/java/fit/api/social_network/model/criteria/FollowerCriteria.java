package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.*;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class FollowerCriteria {
    private Long id;
    private Long userId;
    private Long followingUserId;

    public Specification<Followers> getSpecification() {
        return new Specification<Followers>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Followers> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getUserId() != null) {
                    Join<Followers, User> joinSeller = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getUserId()));
                }
                if (getFollowingUserId() != null) {
                    Join<Followers, User> joinSeller = root.join("followingUser", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getFollowingUserId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
