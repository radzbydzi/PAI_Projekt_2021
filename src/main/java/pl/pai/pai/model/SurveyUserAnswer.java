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
@Table(name = "survey_user_answer")
public class SurveyUserAnswer {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;	
	
	String answer;
	
	@ManyToOne(fetch = FetchType.EAGER)
	SurveyQuestion surveyQuestion;
	
	@ManyToOne(fetch = FetchType.EAGER)
	SurveyUsersAnswers surveyUsersAnswers;

	public SurveyUserAnswer(String answer, SurveyUsersAnswers surveyUsersAnswers, SurveyQuestion surveyQuestion) {
		super();
		this.answer = answer;
		this.surveyUsersAnswers = surveyUsersAnswers;
		this.surveyQuestion = surveyQuestion;
	}
	
	
}
