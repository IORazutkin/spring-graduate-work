package com.iorazutkin.graduatework.entity.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.task.AnswerView;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
public class Answer {
  @EmbeddedId
  @JsonView(AnswerView.class)
  private AnswerPK id;

  @Column
  @JsonView(AnswerView.class)
  private Integer answer;
}
