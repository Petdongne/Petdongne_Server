package org.songeun.petdongne_server.survey.infrastructure;

import org.songeun.petdongne_server.survey.domain.entity.SurveyOptionStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyOptionStatRepository extends JpaRepository<SurveyOptionStat, Long> {

}
