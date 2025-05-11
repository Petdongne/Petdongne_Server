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
public class SurveyOption extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Size(min = 1, max = 150)
    private String optionText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_question_id")
    private SurveyQuestion question;

    public static SurveyOption of(String optionText, SurveyQuestion question){

        return SurveyOption.builder()
                .optionText(optionText)
                .question(question).build();
    }

    @Builder
    private SurveyOption(String optionText, SurveyQuestion question) {
        this.optionText = optionText;
        this.question = question;
    }

}
