package com.iorazutkin.graduatework.entity.institute;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String title;

  @Column
  private String num;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "institute_id")
  private Institute institute;
}
