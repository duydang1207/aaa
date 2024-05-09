package fit.api.social_network.repository;

import fit.api.social_network.model.entity.Room;
import fit.api.social_network.model.entity.RoomMember;
import fit.api.social_network.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember,Long>, JpaSpecificationExecutor<RoomMember> {
    List<RoomMember> findAllByRoom(Room room);
    RoomMember findByRoomAndUser(Room room, User user);
    @Query("select rm from RoomMember rm where rm.room=?1 and rm.user!=?2")
    RoomMember findByRoomNotUser(Room room, User user);
    @Query("select rm.user from RoomMember rm where rm.room=?1 and rm.user!=?2")
    List<User> findAllNotUser(Room room,User user);
}
