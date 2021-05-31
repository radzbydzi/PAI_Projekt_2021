package pl.pai.pai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import pl.pai.pai.model.Quiz;
import pl.pai.pai.model.QuizUsersAnswers;
import pl.pai.pai.model.User;
import pl.pai.pai.repo.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public User addUser(User user)
    {
        return userRepository.save(user);
    }

    public User addUser(String name, String surname, String email, String password)
    {
        return userRepository.save(new User(name,surname,email,password));
    }

    public boolean delUser(Long id)
    {
        try{
            userRepository.deleteById(id);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }
    public boolean delUser(User user)
    {
        try{
            userRepository.delete(user);
            return true;
        }catch (IllegalArgumentException e)
        {
            return false;
        }
    }

    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public Optional<User> get(Long id)
    {
        return getAll().stream().filter(x-> x.getId() == id).findFirst();
    }
}
