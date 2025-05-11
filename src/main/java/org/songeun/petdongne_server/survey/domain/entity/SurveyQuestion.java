package org.songeun.petdongne_server.survey.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyQuestion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String questionText;

    public static SurveyQuestion of(String questionText){

        return SurveyQuestion.builder().
                questionText(questionText).build();
    }

    @Builder
    private SurveyQuestion(String questionText) {
        this.questionText = questionText;
    }
}
