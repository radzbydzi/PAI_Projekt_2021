package pl.pai.pai.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "surveys")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String description;
    @OneToMany(
        mappedBy = "survey",
        cascade = CascadeType.ALL
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    List<SurveyQuestion> questions = new ArrayList<>();

    @OneToMany(
            mappedBy = "userSurvey",
            cascade = CascadeType.ALL
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonIgnore
    List<SurveyUsersAnswers> surveyUsersAnswers = new ArrayList<>();

    boolean forEveryone = true;


    @ManyToOne(fetch = FetchType.EAGER)
    User author;

    String hashLink;

    public Survey(String title, String description, boolean forEveryone, User author, String hashLink) {
        this.title = title;
        this.description = description;
        this.forEveryone = forEveryone;
        this.author = author;
        this.hashLink = hashLink;
    }
}
