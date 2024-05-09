package fit.api.social_network.repository;

import fit.api.social_network.model.entity.Message;
import fit.api.social_network.model.entity.Room;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long>, JpaSpecificationExecutor<Message> {
    @Query("select m from Message m where m.room=?1 order by m.createdDate desc ")
    List<Message> findLastByRoom(Room room);
}
