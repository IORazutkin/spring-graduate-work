package com.iorazutkin.graduatework.entity.practice;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Work {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String projectUrl;

  @Column
  private String teacherComment;

  public Work () {}

  public Work (String projectUrl) {
    this.projectUrl = projectUrl;
  }
}
