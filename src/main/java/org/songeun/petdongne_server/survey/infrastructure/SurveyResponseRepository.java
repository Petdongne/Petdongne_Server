package org.songeun.petdongne_server.survey.infrastructure;

import org.songeun.petdongne_server.survey.domain.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {

}
