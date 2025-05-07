package org.songeun.petdongne_server.survey.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.survey.application.SurveyService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

}
