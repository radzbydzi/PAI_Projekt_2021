package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.QuizQuestion;
import pl.pai.pai.model.QuizUserAnswer;
import pl.pai.pai.model.QuizUsersAnswers;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.repo.QuizUserAnswerRepository;
import pl.pai.pai.repo.QuizUsersAnswersRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuizUserAnswerService {
    @Autowired
    QuizUserAnswerRepository quizUserAnswerRepository;

    @Autowired
    QuizService quizService;

    @Autowired
    UserService userService;

    public QuizUserAnswer addQuizUsersAnswers(QuizUserAnswer quizUserAnswer)
    {
        return quizUserAnswerRepository.save(quizUserAnswer);
    }


    public boolean delQuizUsersAnswers(Long id)
    {
        try{
            quizUserAnswerRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public boolean delQuizUsersAnswers(QuizUserAnswer quiz)
    {
        try{
            quizUserAnswerRepository.delete(quiz);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<QuizUserAnswer> getAll()
    {
        return quizUserAnswerRepository.findAll();
    }

    public Optional<QuizUserAnswer> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId()==id).findFirst();
    }
}
