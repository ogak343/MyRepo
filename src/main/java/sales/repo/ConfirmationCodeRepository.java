package sales.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sales.entity.ConfirmationCodeEntity;

import java.util.Optional;


@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCodeEntity, Long> {

    @Query("select cc from ConfirmationCodeEntity cc join UserEntity u on cc.userId = u.id " +
            "where u.username =:username order by cc.createdAt desc limit 1")
    Optional<ConfirmationCodeEntity> findLastByUsername(@Param("username") String username);

    @Modifying
    @Query(nativeQuery = true, value = "delete from confirmation_codes where user_id =:userId")
    void deleteByUserId(@Param("userId") Long id);
}
