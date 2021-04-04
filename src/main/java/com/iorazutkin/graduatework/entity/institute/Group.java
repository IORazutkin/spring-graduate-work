package com.iorazutkin.graduatework.entity.institute;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.institute.GroupView;
import com.iorazutkin.graduatework.view.practice.PracticeView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "grp")
public class Group {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(GroupView.class)
  private Long id;

  @Column
  @JsonView(GroupView.class)
  private String num;

  @Column
  private Integer year;

  @ManyToOne
  @JoinColumn(name = "course_id")
  private Course course;
}
