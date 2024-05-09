package fit.api.social_network.repository;

import fit.api.social_network.model.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationsRepository extends JpaRepository<Notifications,Long>, JpaSpecificationExecutor<Notifications> {
    List<Notifications> findAllByUserIdAndState(Long userId, Integer state);
    @Transactional
    @Modifying
    void deleteAllByUserId(Long userId);
}
