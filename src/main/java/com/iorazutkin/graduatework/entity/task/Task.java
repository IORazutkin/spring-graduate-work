package com.iorazutkin.graduatework.entity.task;

import com.iorazutkin.graduatework.entity.practice.Practice;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String title;

  @ManyToOne
  @JoinColumn(name = "practice_id")
  private Practice practice;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "theory_id")
  private Theory theory;

  @Column
  private LocalDate finishDate;
}
