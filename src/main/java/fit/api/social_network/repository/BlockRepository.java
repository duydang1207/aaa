package fit.api.social_network.repository;

import fit.api.social_network.model.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long>, JpaSpecificationExecutor<Block> {
    Block findFirstByUserIdAndBlockUserId(Long userId, Long blockUser);
}
