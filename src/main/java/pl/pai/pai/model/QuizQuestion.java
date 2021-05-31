package pl.pai.pai.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.pai.pai.model.enums.QuestionType;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_questions")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String question;

    //sprawdzane tylko dla CLOSED, dal OPENED jest jedna odpowiedz czy dlugie czy krotkie pole
    @OneToMany(
            mappedBy = "quizQuestion",
            cascade = CascadeType.ALL
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    List<QuizAnswer> answers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    Quiz quiz;

    @OneToMany(
            mappedBy = "quizQuestion",
            cascade = CascadeType.ALL
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    List<QuizUserAnswer> quizUserAnswers;
    
    
    QuestionType type;
    
    boolean isLong=false;

    int points=0;
    
    public int getGoodAnswerCount()
    {
        return (int) answers.stream().filter(x -> x.isCorrect).count();
    }

    public List<QuizAnswer> getShuffledAnswers()
    {
    	List<QuizAnswer> res = new ArrayList<>();
    	res.addAll(answers);
    	if(quiz.isRandomOrder())
    	{
            Collections.shuffle(res);
    	}
        return res;
    }

    public QuizQuestion(String question, Quiz quiz, QuestionType type) {
        this.question = question;
        this.quiz = quiz;
        this.type = type;
    }
    public QuizQuestion(String question, QuestionType type) {
        this.question = question;
        this.type = type;
    }
    public String toString() {
    	return Long.toString(id);
    }
}
