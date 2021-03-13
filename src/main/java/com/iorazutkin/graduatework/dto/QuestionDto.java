package com.iorazutkin.graduatework.dto;

import com.iorazutkin.graduatework.entity.task.Question;
import lombok.Data;

@Data
public class QuestionDto {
  Question question;
  Integer answer;

  public QuestionDto (Question question) {
    this.question = question;
  }
}
