package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.QuizQuestion;
import pl.pai.pai.model.QuizUsersAnswers;
import pl.pai.pai.model.User;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.repo.QuizUsersAnswersRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuizUsersAnswersService {
    @Autowired
    QuizUsersAnswersRepository quizUsersAnswersRepository;

    @Autowired
    QuizService quizService;

    @Autowired
    UserService userService;

    public QuizUsersAnswers addQuizUsersAnswers(QuizUsersAnswers quizUsersAnswers)
    {
        return quizUsersAnswersRepository.save(quizUsersAnswers);
    }

    public QuizUsersAnswers addQuizUsersAnswers(@RequestParam Long userId, @RequestParam Long quizId)
    {
        Optional<Quiz> quiz = quizService.get(quizId);
        Optional<User> user = userService.get(quizId);

        if(quiz.isPresent() && user.isPresent())
        {
            return quizUsersAnswersRepository.save(new QuizUsersAnswers(user.get(), quiz.get()));
        }else{
            return null;
        }
    }

    public boolean delQuizUsersAnswers(Long id)
    {
        try{
            quizUsersAnswersRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public boolean delQuizUsersAnswers(QuizUsersAnswers quiz)
    {
        try{
            quizUsersAnswersRepository.delete(quiz);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<QuizUsersAnswers> getAll()
    {
        return quizUsersAnswersRepository.findAll();
    }

    public Optional<QuizUsersAnswers> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId()==id).findFirst();
    }
}
