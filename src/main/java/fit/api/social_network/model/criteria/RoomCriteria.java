package fit.api.social_network.model.criteria;

import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.enums.ROLES;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomCriteria {
    private Long id;
    private Integer status;
    private String name;

    public Specification<Room> getSpecification() {
        return new Specification<Room>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Room> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();

                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(!StringUtils.isBlank(getName())){
                    predicates.add(cb.equal(cb.lower(root.get("name")),"%"+ getName()+"%"));
                }
                query.orderBy(cb.desc(root.get("modifiedDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
