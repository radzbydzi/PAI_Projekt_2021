package pl.pai.pai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_users_answers")
public class QuizUsersAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    User user;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(                                           // adnotacja tworzy tabelkę relacji
//            name = "quiz_users_answers_to_quiz_question",                            // nazwa tabeli
//            joinColumns = @JoinColumn(name = "quiz_users_answers_id"),        // FK z tab users
//            inverseJoinColumns = @JoinColumn(name = "quiz_question_id")  // FK z tab roles
//    )
    @OneToMany(
            mappedBy = "quizUsersAnswers",
            cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<QuizUserAnswer> userAnswers = new ArrayList<>(); // zaleznie od typu C_S: id_odpowiedzi C_M: id_odp;id_odp;id_odp OPENED: String

    @ManyToOne(fetch = FetchType.EAGER)
    Quiz userQuiz;

    public QuizUsersAnswers(User user, Quiz userQuiz) {
        this.user = user;
        this.userQuiz = userQuiz;
    }
}
