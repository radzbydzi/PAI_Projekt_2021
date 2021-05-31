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
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey_questions")
public class SurveyQuestion {//tresci odpowiedzi w quizie
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String question;
    //sprawdzane tylko dla CLOSED_SINGLE I CLOSED_MULTIPLE
    @OneToMany(
            mappedBy = "surveyQuestion",
            cascade = CascadeType.ALL
    )

    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    List<SurveyAnswer> answers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    Survey survey;

    QuestionType type;
    
    boolean isLong=false;
    
    public SurveyQuestion(String question, Survey survey, QuestionType type) {
        this.question = question;
        this.survey = survey;
        this.type = type;
    }
    
    public SurveyQuestion(String question, QuestionType type) {
        this.question = question;
        this.type = type;
    }
    public String toString() {
    	return Long.toString(id);
    }
}
