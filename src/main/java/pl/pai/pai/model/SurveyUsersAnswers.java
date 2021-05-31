package pl.pai.pai.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "survey_users_answers")
public class SurveyUsersAnswers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    User user;

    @OneToMany(
            mappedBy = "surveyUsersAnswers",
            cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    List<SurveyUserAnswer> userAnswers = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    Survey userSurvey;

    public SurveyUsersAnswers(User user, Survey userSurvey) {
        this.user = user;
        this.userSurvey = userSurvey;
    }
}
