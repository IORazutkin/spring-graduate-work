package com.iorazutkin.graduatework.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.view.task.TaskView;
import lombok.Data;

@Data
public class TaskWithResultDto {
  @JsonView(TaskView.class)
  private Task task;
  @JsonView(TaskView.class)
  private Integer score;
  @JsonView(TaskView.class)
  private Integer count;

  public TaskWithResultDto () {}

  public TaskWithResultDto (Task task, Integer score, Integer count) {
    this.task = task;
    this.score = score;
    this.count = count;
  }
}
