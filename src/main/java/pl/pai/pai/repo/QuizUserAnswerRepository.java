package pl.pai.pai.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.pai.pai.model.QuizUserAnswer;
import pl.pai.pai.model.QuizUsersAnswers;

@Repository
public interface QuizUserAnswerRepository extends JpaRepository<QuizUserAnswer, Long> {
}
