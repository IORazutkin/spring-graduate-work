package com.iorazutkin.graduatework.entity.task;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Question {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String question;

  @ElementCollection
  private List<String> answers = new ArrayList<>();

  @Column
  private Integer rightAnswer;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task;
}
