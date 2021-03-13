package com.iorazutkin.graduatework.entity.practice;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Theme {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String title;

  @ManyToOne
  @JoinColumn(name = "practice_id")
  private Practice practice;
}
