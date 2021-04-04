package com.iorazutkin.graduatework.entity.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.task.AnswerView;
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
  @JsonView(AnswerView.class)
  private Question question;

  public AnswerPK () {}

  public AnswerPK (TaskResult taskResult, Question question) {
    this.question = question;
    this.taskResult = taskResult;
  }
}
