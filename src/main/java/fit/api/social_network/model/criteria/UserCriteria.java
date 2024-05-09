package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Followers;
import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.enums.ROLES;
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
public class UserCriteria {
    private Long id;
    private Integer status;
    private String name;
    private String email;
    private String bio;
    private Integer kind;
    private ROLES role;
    private Integer createdStoryHour;
    private Long followOfUserId;

    public Specification<User> getSpecification() {
        return new Specification<User>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(!StringUtils.isBlank(getName())){
                    predicates.add(cb.like(cb.lower(root.get("name")),"%"+ getName().toLowerCase()+"%"));
                }
                if(!StringUtils.isBlank(getEmail())){
                    predicates.add(cb.equal(cb.lower(root.get("email")),"%"+ getEmail()+"%"));
                }
                if(!StringUtils.isBlank(getBio())){
                    predicates.add(cb.equal(cb.lower(root.get("bio")),"%"+ getBio()+"%"));
                }
                if (getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if (getRole() != null) {
                    predicates.add(cb.equal(root.get("role"), getRole()));
                }
                if(getCreatedStoryHour() != null){
                    LocalDateTime currentTime = LocalDateTime.now();
                    LocalDateTime twentyFourHoursAgo = currentTime.minusHours(getCreatedStoryHour());

                    Date dateTwentyFourHoursAgo = Date.from(twentyFourHoursAgo.atZone(ZoneId.systemDefault()).toInstant());
                    Date currentDate = Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant());

                    Subquery<Long> userIdSubquery = query.subquery(Long.class);
                    Root<Posts> postsRoot = userIdSubquery.from(Posts.class);
                    userIdSubquery.select(postsRoot.get("user").get("id"));

                    Predicate userKindPredicate = cb.equal(postsRoot.get("kind"), 2);
                    Predicate datePredicate = cb.between(postsRoot.get("createdDate"), dateTwentyFourHoursAgo, currentDate);

                    userIdSubquery.where(cb.and(userKindPredicate, datePredicate));

                    predicates.add(root.get("id").in(userIdSubquery));
                }
                if(getFollowOfUserId() != null){
                    Subquery<Long> userIdSubquery = query.subquery(Long.class);
                    Root<Followers> followersRoot = userIdSubquery.from(Followers.class);
                    userIdSubquery.select(followersRoot.get("user").get("id"));
                    Predicate userKindPredicate = cb.equal(followersRoot.get("followingUser").get("id"), getFollowOfUserId());
                    userIdSubquery.where(cb.and(userKindPredicate));
                    predicates.add(root.get("id").in(userIdSubquery));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
