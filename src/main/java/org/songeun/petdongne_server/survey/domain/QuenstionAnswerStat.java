package org.songeun.petdongne_server.survey.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.building.domain.Building;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuenstionAnswerStat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    private Integer selectedCount;

    @NotNull
    private Integer percentage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "answer_id", nullable = false)
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Integer selectedCount() {
        return this.selectedCount;
    }

    @Builder
    private QuenstionAnswerStat(
            Integer selectedCount,
            Building building,
            Answer answer,
            Question question,
            Integer percentage
    ) {
        this.selectedCount = selectedCount;
        this.building = building;
        this.answer = answer;
        this.question = question;
        this.percentage = percentage;
    }

}
