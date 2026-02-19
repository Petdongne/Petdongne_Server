package org.songeun.petdongne_server.survey.infrastructure;

import org.songeun.petdongne_server.survey.domain.SelectedAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectedAnswerRepository extends JpaRepository<SelectedAnswer, Long> {

}
