package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.entity.MessageViews;
import fit.api.social_network.model.entity.Notifications;
import fit.api.social_network.model.entity.User;
import jakarta.persistence.criteria.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotificationCriteria {
    private Long id;
    private Integer status;
    private Long userId;
    private String notification;

    public Specification<Notifications> getSpecification() {
        return new Specification<Notifications>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Notifications> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getUserId() != null) {
                    Join<Notifications, User> joinSeller = root.join("user", JoinType.INNER);
                    predicates.add(cb.equal(joinSeller.get("id"), getUserId()));
                }
                if(!StringUtils.isBlank(getNotification())){
                    predicates.add(cb.equal(cb.lower(root.get("notification")),"%"+ getNotification()+"%"));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
