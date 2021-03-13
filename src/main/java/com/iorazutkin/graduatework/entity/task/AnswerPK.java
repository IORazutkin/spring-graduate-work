package com.iorazutkin.graduatework.entity.task;

import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class AnswerPK implements Serializable {
  @ManyToOne
  @JoinColumn(name = "test_result_id")
  private TaskResult taskResult;

  @ManyToOne
  @JoinColumn(name = "question_id")
  private Question question;

  public AnswerPK () {}

  public AnswerPK (TaskResult taskResult, Question question) {
    this.question = question;
    this.taskResult = taskResult;
  }
}
