package fit.api.social_network.repository;

import fit.api.social_network.model.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<Posts,Long>, JpaSpecificationExecutor<Posts> {
    @Query("SELECT COUNT(l) FROM Likes l WHERE l.post.id = :postId")
    int getLikeAmount(@Param("postId") Long postId);
    Posts findFirstByIdAndUserId(Long id, Long userId);


    @Query("SELECT p from Posts p " +
            "JOIN RoomMember r on r.user = p.user " +
            "join Message m on m.room = r.room " +
            "where  p.user.id!=?1 " +
            "and p.id not in (select l.post.id from Likes l where l.user.id=?1) " +
            "union " +
            "select p from Posts p " +
            "join Followers f on f.followingUser=p.user where f.user.id=?1 and p.id not in (select l.post.id from Likes l where l.user.id=?1)")
    List<Posts> findAllByUserId(Long userId);

}
