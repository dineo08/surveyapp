package com.project.app.survey.surveyprojectapp.service;

import com.project.app.survey.surveyprojectapp.entity.SurveyUser;
import com.project.app.survey.surveyprojectapp.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class SurveyService {
    private SurveyRepository surveyRepository;

    public SurveyUser saveResponse(SurveyUser surveyUser) {
        int age = calculateAge(surveyUser.getBirthdate());
        surveyUser.setAge(age);
        return surveyRepository.save(surveyUser);
    }

    public List<SurveyUser> getAllSurveys() {
        return surveyRepository.findAll();
    }

    public long getTotalSurveys() {
        return surveyRepository.count();
    }

    public double getAverageAge() {
        double avg = surveyRepository.findAll().stream()
                .mapToInt(user -> Period.between(user.getBirthdate(), LocalDate.now()).getYears())
                .average()
                .orElse(0.0);

        return Math.ceil(avg);
    }

    public int getOldestAge() {
        return surveyRepository.findAll().stream()
                .mapToInt(user -> Period.between(user.getBirthdate(), LocalDate.now()).getYears())
                .max()
                .orElse(0);
    }

    public int getYoungestAge() {
        return surveyRepository.findAll().stream()
                .mapToInt(user -> Period.between(user.getBirthdate(), LocalDate.now()).getYears())
                .min()
                .orElse(0);
    }

    public double getFoodPercentage(String foodName) {
        long totalSurveys = surveyRepository.count();
        if (totalSurveys == 0) return 0;

        long count = surveyRepository.findAll().stream()
                .filter(user -> user.getFavouriteFoods() != null && user.getFavouriteFoods().contains(foodName))
                .count();

        return Math.ceil(count * 100.0 / totalSurveys); // round to 1 decimal place
    }


    public double getAverageRating(String category) {
        List<SurveyUser> all = surveyRepository.findAll();
        if (all.isEmpty()) return 0;

        double average = switch (category.toLowerCase()) {
            case "eatout" -> all.stream().mapToInt(SurveyUser::getEatOutRating).average().orElse(0);
            case "movies" -> all.stream().mapToInt(SurveyUser::getWatchMoviesRating).average().orElse(0);
            case "tv"     -> all.stream().mapToInt(SurveyUser::getWatchTvRating).average().orElse(0);
            case "radio"  -> all.stream().mapToInt(SurveyUser::getListenToRadioRating).average().orElse(0);
            default       -> 0;
        };

        return Math.ceil(average); // rounded to 1 decimal place
    }


    private int calculateAge(LocalDate birthdate) {
        return Period.between(birthdate, LocalDate.now()).getYears();
    }
}

