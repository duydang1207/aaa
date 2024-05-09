package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.*;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class LikeCriteria {
    private Long id;
    private Long userId;
    private Long postId;
    private Long commentId;
    private Integer kind;
    private Integer postKind;

    public Specification<Likes> getSpecification() {
        return new Specification<Likes>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Likes> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getUserId() != null) {
                    Join<Likes, User> joinSeller = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getUserId()));
                }
                if (getPostId() != null) {
                    Join<Likes, Posts> joinSeller = root.join("post", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getPostId()));
                }
                if (getPostKind() != null) {
                    Join<Likes, Posts> joinSeller = root.join("post", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("kind"), getPostKind()));
                }
                if (getCommentId() != null) {
                    Join<Likes, Comments> joinSeller = root.join("comment", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getCommentId()));
                }
                if (getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                query.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
