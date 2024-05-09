package fit.api.social_network.repository;

import fit.api.social_network.model.entity.Posts;
import fit.api.social_network.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>, JpaSpecificationExecutor<Room> {
    @Query("select r from Room r join RoomMember rm on r.id=rm.room.id join Message m on m.room.id=r.id where rm.user.id=?1 order by m.modifiedDate DESC ")
    List<Room> findRoomsByUser(Long userId);

}
