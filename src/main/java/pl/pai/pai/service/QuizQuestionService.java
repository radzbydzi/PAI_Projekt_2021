package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.QuizAnswer;
import pl.pai.pai.model.QuizQuestion;
import pl.pai.pai.model.enums.QuestionType;
import pl.pai.pai.repo.QuizQuestionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuizQuestionService {
    @Autowired
    QuizQuestionRepository quizQuestionRepository;

    @Autowired
    QuizService quizService;

    public QuizQuestion addQuizQuestion(QuizQuestion quizQuestion)
    {
        return quizQuestionRepository.save(quizQuestion);
    }
    public QuizQuestion addQuizQuestion(@RequestParam String question, @RequestParam Long quizId, @RequestParam QuestionType type)
    {
        Optional<Quiz> quiz = quizService.get(quizId);
        if(quiz.isPresent())
        {
            return quizQuestionRepository.save(new QuizQuestion(question, quiz.get(), type));
        }else{
            return null;
        }
    }

    public boolean delQuizQuestion(Long id)
    {
        try{
            quizQuestionRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public boolean delQuizQuestion(QuizQuestion quiz)
    {
        try{
            quizQuestionRepository.delete(quiz);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<QuizQuestion> getAll()
    {
        return quizQuestionRepository.findAll();
    }

    public Optional<QuizQuestion> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId()==id).findFirst();
    }
}
