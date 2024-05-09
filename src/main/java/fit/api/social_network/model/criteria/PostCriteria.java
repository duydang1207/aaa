package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.entity.User;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class PostCriteria {
    private Long id;
    private Integer status;
    private Long userId;
    private String image_url;
    private String caption;
    private Integer kind;
    private Integer likedAmount;
    private Boolean isSortTopLike;
    private Integer createdHour;

    public Specification<Posts> getSpecification() {
        return new Specification<Posts>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Posts> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getLikedAmount() != null) {
                    predicates.add(cb.equal(root.get("likedAmount"), getStatus()));
                }
                if (getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(!StringUtils.isBlank(getImage_url())){
                    predicates.add(cb.equal(cb.lower(root.get("image_url")),"%"+ getImage_url()+"%"));
                }
                if (getUserId() != null) {
                    Join<Posts, User> joinSeller = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getUserId()));
                }
                if(!StringUtils.isBlank(getCaption())){
                    predicates.add(cb.like(cb.lower(root.get("caption")),"%"+ getCaption().toLowerCase()+"%"));
                }
                if(Boolean.TRUE.equals(getIsSortTopLike())) {
                    query.orderBy(cb.desc(root.get("likedAmount")));
                }
                if (getCreatedHour() != null) {
                    LocalDateTime currentTime = LocalDateTime.now();
                    LocalDateTime twentyFourHoursAgo = currentTime.minusHours(getCreatedHour());

                    Date dateTwentyFourHoursAgo = Date.from(twentyFourHoursAgo.atZone(ZoneId.systemDefault()).toInstant());
                    Date currentDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

                    predicates.add(cb.between(root.get("createdDate"), dateTwentyFourHoursAgo, currentDate));
                }

                query.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
