package pl.pai.pai.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.pai.pai.model.QuizUsersAnswers;

@Repository
public interface QuizUsersAnswersRepository extends JpaRepository<QuizUsersAnswers, Long> {
}
