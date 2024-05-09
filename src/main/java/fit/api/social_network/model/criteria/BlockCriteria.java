package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Block;
import fit.api.social_network.model.entity.User;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlockCriteria {
    private Long id;
    private Long userId;
    private Long blockUserId;

    public Specification<Block> getSpecification() {
        return new Specification<Block>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Block> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getUserId() != null) {
                    Join<Block, User> joinSeller = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getUserId()));
                }
                if (getBlockUserId() != null) {
                    Join<Block, User> joinSeller = root.join("blockUser", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getBlockUserId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
