package pl.pai.pai.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "quiz_user_answer")
public class QuizUserAnswer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;	
	
	String answer;
	boolean checked=false;
	int points=0;
	
	@ManyToOne(fetch = FetchType.EAGER)
	QuizQuestion quizQuestion;
	
	@ManyToOne(fetch = FetchType.EAGER)
	QuizUsersAnswers quizUsersAnswers;

	public QuizUserAnswer(String answer, boolean checked, int points, QuizUsersAnswers quizUsersAnswers, QuizQuestion quizQuestion) {
		super();
		this.answer = answer;
		this.checked = checked;
		this.points = points;
		this.quizUsersAnswers = quizUsersAnswers;
		this.quizQuestion = quizQuestion;
	}
	
	
}
