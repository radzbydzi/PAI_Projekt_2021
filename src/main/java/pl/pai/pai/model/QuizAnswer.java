package pl.pai.pai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_answers")
public class QuizAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;
    boolean isCorrect;

    @ManyToOne(fetch = FetchType.EAGER)
    QuizQuestion quizQuestion;

    public QuizAnswer(String description, boolean isCorrect) {
        this.description = description;
        this.isCorrect = isCorrect;
    }
    public QuizAnswer(String description, boolean isCorrect, QuizQuestion quizQuestion) {
        this.description = description;
        this.isCorrect = isCorrect;
        this.quizQuestion = quizQuestion;
    }
}
