package fit.api.social_network.repository;

import fit.api.social_network.model.entity.MessageViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageViewsRepository extends JpaRepository<MessageViews,Long>, JpaSpecificationExecutor<MessageViews> {
}
