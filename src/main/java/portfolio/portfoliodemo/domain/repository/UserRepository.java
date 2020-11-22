package portfolio.portfoliodemo.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import portfolio.portfoliodemo.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
