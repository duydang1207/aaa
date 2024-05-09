package fit.api.social_network.repository;

import fit.api.social_network.model.entity.Comments;
import fit.api.social_network.model.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long>, JpaSpecificationExecutor<Comments> {
    List<Comments> findAllByPost(Posts posts);
    List<Comments> findAllByParentId(Long parentId);
}
