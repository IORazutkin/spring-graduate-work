package com.iorazutkin.graduatework.entity.task;

import com.iorazutkin.graduatework.entity.Student;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class TaskResult {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "student_id")
  private Student student;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task;

  @Column
  private Integer score;

  public TaskResult () {}

  public TaskResult (Student student, Task task) {
    this.student = student;
    this.task = task;
  }
}
