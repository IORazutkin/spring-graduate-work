package com.iorazutkin.graduatework.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.task.Question;
import com.iorazutkin.graduatework.view.task.QuestionTestView;
import lombok.Data;

@Data
public class QuestionDto {
  @JsonView(QuestionTestView.class)
  Question question;
  @JsonView(QuestionTestView.class)
  Integer answer;

  public QuestionDto (Question question) {
    this.question = question.clone();
  }
}
