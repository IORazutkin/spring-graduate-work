package com.iorazutkin.graduatework.entity.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.view.task.TaskView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(TaskView.class)
  private Long id;

  @Column
  @JsonView(TaskView.class)
  private String title;

  @ManyToOne
  @JoinColumn(name = "practice_id")
  private Practice practice;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "theory_id")
  @JsonView(TaskView.class)
  private Theory theory;

  @Column
  @JsonView(TaskView.class)
  private LocalDate finishDate;

  @Column
  private Boolean deleted = false;

  public Boolean isDateOfOut () {
    return this.getFinishDate().isAfter(LocalDate.now());
  }
}
