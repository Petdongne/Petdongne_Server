package org.songeun.petdongne_server.survey.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.songeun.petdongne_server.global.common.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @NotNull
    @Size(min = 1, max = 255)
    private String content;

    @Builder
    private Question(QuestionType questionType, String content) {
        this.questionType = questionType;
        this.content = content;
    }

    public QuestionType questionType() {
        return questionType;
    }

}
