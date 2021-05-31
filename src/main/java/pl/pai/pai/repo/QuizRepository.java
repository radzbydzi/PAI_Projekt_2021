package pl.pai.pai.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pai.pai.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
