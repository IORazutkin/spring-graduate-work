package com.iorazutkin.graduatework.entity.practice;

import com.iorazutkin.graduatework.entity.Student;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StudentPractice {
  @EmbeddedId
  private StudentPracticePK id;

  @ManyToOne
  @JoinColumn(name = "theme_id")
  private Theme theme;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "work_id")
  private Work work;

  @Column
  private Integer score;

  public StudentPractice () {}

  public StudentPractice (Student student, Practice practice) {
    this.id = new StudentPracticePK(student, practice);
    this.theme = null;
    this.work = null;
    this.score = null;
  }
}
