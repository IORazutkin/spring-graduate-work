package com.iorazutkin.graduatework.entity.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.task.TaskView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Theory {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(TaskView.class)
  private Long id;

  @Column
  @JsonView(TaskView.class)
  private String contentUrl;

  public Theory (String contentUrl) {
    this.contentUrl = contentUrl;
  }

  public Theory () {
  }
}
