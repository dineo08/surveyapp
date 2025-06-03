package com.project.app.survey.surveyprojectapp.repository;

import com.project.app.survey.surveyprojectapp.entity.SurveyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<SurveyUser, Long> {
}
