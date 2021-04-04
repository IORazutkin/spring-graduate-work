package com.iorazutkin.graduatework.entity.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.view.practice.StudentPracticeAuthListView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeAuthView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeListView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StudentPractice {
  @EmbeddedId
  @JsonView({StudentPracticeListView.class, StudentPracticeAuthListView.class})
  private StudentPracticePK id;

  @ManyToOne
  @JoinColumn(name = "theme_id")
  @JsonView({StudentPracticeView.class, StudentPracticeAuthView.class})
  private Theme theme;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "work_id")
  @JsonView({StudentPracticeView.class, StudentPracticeAuthView.class})
  private Work work;

  @Column
  @JsonView({StudentPracticeView.class, StudentPracticeAuthListView.class})
  private Integer score;

  public StudentPractice () {}

  public StudentPractice (Student student, Practice practice) {
    this.id = new StudentPracticePK(student, practice);
    this.theme = null;
    this.work = null;
    this.score = null;
  }
}
