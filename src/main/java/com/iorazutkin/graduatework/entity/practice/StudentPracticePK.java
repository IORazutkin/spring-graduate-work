package com.iorazutkin.graduatework.entity.practice;

import com.iorazutkin.graduatework.entity.Student;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@Embeddable
public class StudentPracticePK implements Serializable {
  @ManyToOne
  @JoinColumn(name = "student_id")
  private Student student;

  @ManyToOne
  @JoinColumn(name = "practice_id")
  private Practice practice;

  public StudentPracticePK () {}

  public StudentPracticePK (Student student, Practice practice) {
    this.student = student;
    this.practice = practice;
  }
}
