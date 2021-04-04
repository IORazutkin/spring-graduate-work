package com.iorazutkin.graduatework.entity.institute;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.institute.CourseView;
import com.iorazutkin.graduatework.view.institute.CourseWithInstituteView;
import com.iorazutkin.graduatework.view.user.UserView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(CourseView.class)
  private Long id;

  @Column
  @JsonView(CourseView.class)
  private String title;

  @Column
  private String num;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "institute_id")
  @JsonView(CourseWithInstituteView.class)
  private Institute institute;
}
