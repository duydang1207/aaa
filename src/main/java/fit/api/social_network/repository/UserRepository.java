package fit.api.social_network.repository;

import fit.api.social_network.model.entity.User;
import fit.api.social_network.model.response.user.GeneralInfoDto;
import fit.api.social_network.model.response.user.GeneralInfoDtoImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);
    Optional<User> findFirstByEmailAndOtp(String email, String opt);
    @Query(value = "select \n" +
            "    (select count(id) from posts where user_id = :userId where kind = 1) as postAmount, \n" +
            "    (select count(id) from followers where user_id = :userId) as followerAmount, \n" +
            "    (select count(id) from followers where following_user_id = :userId) as followingAmount\n",nativeQuery = true)
    GeneralInfoDto getGeneralInfo(Long userId);

}
