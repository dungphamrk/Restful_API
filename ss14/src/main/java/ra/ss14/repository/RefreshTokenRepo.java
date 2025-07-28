package ra.ss14.repository;

import ra.ss14.model.entity.RefreshToken;
import ra.ss14.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long>{
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);

    List<RefreshToken> findAllByUserOrderByExpiryDateAsc(User user);
}