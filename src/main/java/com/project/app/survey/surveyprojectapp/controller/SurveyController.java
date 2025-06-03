package com.project.app.survey.surveyprojectapp.controller;

import com.project.app.survey.surveyprojectapp.entity.SurveyUser;
import com.project.app.survey.surveyprojectapp.service.SurveyService;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Controller
@AllArgsConstructor
public class SurveyController {
    private SurveyService surveyService;

    @GetMapping("/survey_form")
    public String surveyForm(Model model) {
        model.addAttribute("surveyUser", new SurveyUser());
        return "survey_form";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute("surveyUser") SurveyUser surveyUser, BindingResult bindingResult,
                         Model model) {

        if (bindingResult.hasErrors()) {
            return "survey_form";
        }

        // Custom validations

        // Validate Date of Birth and Age
        if (surveyUser.getBirthdate() == null) {
            model.addAttribute("customError", "Date of birth is required.");
            return "survey_form";
        }

        int age = Period.between(surveyUser.getBirthdate(), LocalDate.now()).getYears();
        if (age < 5 || age > 120) {
            model.addAttribute("customError", "Age must be between 5 and 120.");
            return "survey_form";
        }

        // Validate Favorite Foods
        if (surveyUser.getFavouriteFoods() == null || surveyUser.getFavouriteFoods().isEmpty()) {
            model.addAttribute("customError", "Please choose at least one favorite food.");
            return "survey_form";
        } else {
            surveyUser.setFavouriteFoods(surveyUser.getFavouriteFoods());
        }

        // Validate Ratings
        if (!isValidRating(surveyUser.getEatOutRating()) ||
                !isValidRating(surveyUser.getWatchMoviesRating()) ||
                !isValidRating(surveyUser.getWatchTvRating()) ||
                !isValidRating(surveyUser.getListenToRadioRating())) {
            model.addAttribute("customError", "All ratings must be selected (1-5).");
            return "survey_form";
        }

        surveyService.saveResponse(surveyUser);

        return "redirect:/survey_form?success=true";
    }

    private boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }

    @GetMapping("/survey_results")
    public String surveyResults(Model model) {
        List<SurveyUser> allSurveys = surveyService.getAllSurveys();

        if(allSurveys.isEmpty()){
            model.addAttribute("hasData", false);
        } else {
            model.addAttribute("hasData", true);
            model.addAttribute("totalSurveys", surveyService.getTotalSurveys());
            model.addAttribute("averageAge", surveyService.getAverageAge());
            model.addAttribute("oldest", surveyService.getOldestAge());
            model.addAttribute("youngest", surveyService.getYoungestAge());
            model.addAttribute("pizzaPercentage", surveyService.getFoodPercentage("Pizza"));
            model.addAttribute("pastaPercentage", surveyService.getFoodPercentage("Pasta"));
            model.addAttribute("papWorsPercentage", surveyService.getFoodPercentage("Pap and Wors"));
            model.addAttribute("avgMovies", surveyService.getAverageRating("movies"));
            model.addAttribute("avgRadio", surveyService.getAverageRating("radio"));
            model.addAttribute("avgEatOut", surveyService.getAverageRating("eatout"));
            model.addAttribute("avgTv", surveyService.getAverageRating("tv"));
        }
        return "survey_results";
    }
}

