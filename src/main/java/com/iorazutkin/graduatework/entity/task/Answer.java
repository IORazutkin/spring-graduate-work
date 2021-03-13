package com.iorazutkin.graduatework.entity.task;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Data
@Entity
public class Answer {
  @EmbeddedId
  private AnswerPK id;

  @Column
  private Integer answer;
}
