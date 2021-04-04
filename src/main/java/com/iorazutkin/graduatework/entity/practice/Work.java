package com.iorazutkin.graduatework.entity.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeAuthView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Work {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({ StudentPracticeView.class, StudentPracticeAuthView.class})
  private Long id;

  @Column
  @JsonView({ StudentPracticeView.class, StudentPracticeAuthView.class})
  private String projectUrl;

  @Column
  @JsonView({ StudentPracticeView.class, StudentPracticeAuthView.class})
  private String teacherComment;

  public Work () {}

  public Work (String projectUrl) {
    this.projectUrl = projectUrl;
  }
}
