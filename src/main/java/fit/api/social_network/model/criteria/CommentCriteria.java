package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Comments;
import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.entity.User;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentCriteria {
    private Long id;
    private Long userId;
    private Long postId;
    private Long parentId;
    private Integer kind;
    private String comment;

    public Specification<Comments> getSpecification() {
        return new Specification<Comments>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Comments> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if (getParentId() != null) {
                    Join<Comments, Comments> joinSeller = root.join("parent", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getParentId()));
                }
                if (getUserId() != null) {
                    Join<Comments, User> joinSeller = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getUserId()));
                }
                if (getPostId() != null) {
                    Join<Comments, Posts> joinSeller = root.join("post", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getPostId()));
                }
                if(!StringUtils.isBlank(getComment())){
                    predicates.add(cb.equal(cb.lower(root.get("comment")),"%"+ getComment()+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
