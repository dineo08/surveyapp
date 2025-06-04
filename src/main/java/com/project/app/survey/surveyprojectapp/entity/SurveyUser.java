package com.project.app.survey.surveyprojectapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SurveyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Fullnames are required")
    private String fullname;

    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Contact number is required")
    private String phonenumber;

    @NotNull(message = "Date of birth is required")
    private LocalDate birthdate;

    private int age;

    @ElementCollection
    @CollectionTable(name = "favourite_foods", joinColumns = @JoinColumn(name = "survey_id"))
    @Column(name = "food")
    private List<String> favouriteFoods;

    @Min(1)
    @Max(5)
    private Integer eatOutRating;

    @Min(1)
    @Max(5)
    private Integer watchMoviesRating;

    @Min(1)
    @Max(5)
    private Integer watchTvRating;

    @Min(1)
    @Max(5)
    private Integer listenToRadioRating;
}

