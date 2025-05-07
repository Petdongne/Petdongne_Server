package org.songeun.petdongne_server.review.presentation;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.review.application.ResidenceReviewService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResidenceReviewController {

    private final ResidenceReviewService residenceReviewService;

}
