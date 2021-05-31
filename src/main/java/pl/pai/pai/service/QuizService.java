package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.User;
import pl.pai.pai.repo.QuizRepository;

import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    UserService userService;

    public Quiz addQuiz(Quiz quiz)
    {
        return quizRepository.save(quiz);
    }
    public Quiz addQuiz(String title, String description, boolean randomOrder, Long durationInSeconds, boolean forEveryone, Long authorId, String hashLink)
    {
        Optional<User> user = userService.get(authorId);
        if(user.isPresent())
        {
            return quizRepository.save(new Quiz(title,description,randomOrder,durationInSeconds,forEveryone, user.get(), hashLink));
        }else{
            return null;
        }
    }

    public boolean delQuiz(Long id)
    {
        try{
            quizRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public boolean delQuiz(Quiz quiz)
    {
        try{
            quizRepository.delete(quiz);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<Quiz> getAll()
    {
        return quizRepository.findAll();
    }

    public Optional<Quiz> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId()==id).findFirst();
    }

}
