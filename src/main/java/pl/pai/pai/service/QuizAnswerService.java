package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.QuizAnswer;
import pl.pai.pai.model.QuizQuestion;
import pl.pai.pai.model.User;
import pl.pai.pai.repo.QuizAnswerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuizAnswerService {
    @Autowired
    QuizAnswerRepository quizAnswerRepository;

    @Autowired
    QuizQuestionService quizQuestionService;

    public QuizAnswer addQuizAnswer(QuizAnswer quizAnswer)
    {
        return quizAnswerRepository.save(quizAnswer);
    }
    public QuizAnswer addQuizAnswer(String description, boolean isCorrect, Long quizQuestionId)
    {
        Optional<QuizQuestion> quizQuestion = quizQuestionService.get(quizQuestionId);
        if(quizQuestion.isPresent())
        {
            return quizAnswerRepository.save(new QuizAnswer(description,isCorrect,quizQuestion.get()));
        }else{
            return null;
        }
    }

    public boolean delQuizAnswer(Long id)
    {
        try{
            quizAnswerRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public boolean delQuizAnswer(QuizAnswer quiz)
    {
        try{
            quizAnswerRepository.delete(quiz);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<QuizAnswer> getAll()
    {
        return quizAnswerRepository.findAll();
    }

    public Optional<QuizAnswer> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId()==id).findFirst();
    }
}
