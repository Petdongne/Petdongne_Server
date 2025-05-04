package org.songeun.petdongne_server.review.application;

import lombok.RequiredArgsConstructor;
import org.songeun.petdongne_server.review.infrastructure.ResidenceReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResidenceReviewService {

    private final ResidenceReviewRepository reviewRepository;

}
