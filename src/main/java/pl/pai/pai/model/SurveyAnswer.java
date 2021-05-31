package pl.pai.pai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey_answers")
public class SurveyAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;

    @ManyToOne(fetch = FetchType.EAGER)
    SurveyQuestion surveyQuestion;

    public SurveyAnswer(String description, SurveyQuestion surveyQuestion) {
        this.description = description;
        this.surveyQuestion = surveyQuestion;
    }
}
