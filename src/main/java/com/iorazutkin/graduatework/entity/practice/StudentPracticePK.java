package com.iorazutkin.graduatework.entity.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.view.practice.StudentPracticeAuthListView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeListView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeView;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class StudentPracticePK implements Serializable {
  @ManyToOne
  @JoinColumn(name = "student_id")
  @JsonView(StudentPracticeListView.class)
  private Student student;

  @ManyToOne
  @JoinColumn(name = "practice_id")
  @JsonView({StudentPracticeView.class, StudentPracticeAuthListView.class})
  private Practice practice;

  public StudentPracticePK () {}

  public StudentPracticePK (Student student, Practice practice) {
    this.student = student;
    this.practice = practice;
  }
}
