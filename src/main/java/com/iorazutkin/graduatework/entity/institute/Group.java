package com.iorazutkin.graduatework.entity.institute;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "grp")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String num;

  @Column
  private Integer year;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;
}
