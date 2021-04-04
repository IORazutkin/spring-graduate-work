package com.iorazutkin.graduatework.entity.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.practice.ThemeView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Theme {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(ThemeView.class)
  private Long id;

  @Column
  @JsonView(ThemeView.class)
  private String title;

  @ManyToOne
  @JoinColumn(name = "practice_id")
  private Practice practice;

  @Column
  private Boolean deleted = false;
}
