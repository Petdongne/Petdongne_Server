package org.songeun.petdongne_server.survey.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AnswerOption answerOption;

    public AnswerOption option() {
        return this.answerOption;
    }

    @Builder
    private Answer(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }

}
