package sales.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sales.entity.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsername(String username);

    boolean existsByUsername(String number);

    @Query(nativeQuery = true, value = "select enabled from users where username = :email")
    boolean checkStatusByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("update UserEntity u set u.enabled = TRUE where u.username =:username")
    void enableUser(@Param("username") String username);

}
