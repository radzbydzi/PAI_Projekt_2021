package pl.pai.pai.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pai.pai.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
