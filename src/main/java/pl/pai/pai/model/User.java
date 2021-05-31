package pl.pai.pai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String surname;
    String email;
    String password;


    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    List<Quiz> quizList = new ArrayList<>();


    @OneToMany(
            mappedBy = "author",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    List<Survey> surveyList = new ArrayList<>();

    @OneToMany(
            mappedBy = "userQuiz",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    List<QuizUsersAnswers> quizUsersAnswersList = new ArrayList<>();


    @OneToMany(
            mappedBy = "userSurvey",
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    List<SurveyUsersAnswers> surveyUsersAnswersList = new ArrayList<>();

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
}
