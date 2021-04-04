package com.iorazutkin.graduatework.entity.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.task.QuestionTestView;
import com.iorazutkin.graduatework.view.task.QuestionView;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Question implements Cloneable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(QuestionTestView.class)
  private Long id;

  @Column
  @JsonView(QuestionTestView.class)
  private String question;

  @ElementCollection
  @JsonView(QuestionTestView.class)
  private List<String> answers = new ArrayList<>();

  @Column
  @JsonView(QuestionView.class)
  private Integer rightAnswer;

  @ManyToOne
  @JoinColumn(name = "task_id")
  private Task task;

  public Boolean isDateOfOut () {
    return this.getTask().getFinishDate().compareTo(LocalDate.now()) < 0;
  }

  @Override
  public Question clone () {
    try {
      return (Question) super.clone();
    } catch (CloneNotSupportedException ignore) {}

    Question question = new Question();
    BeanUtils.copyProperties(this, question);

    return question;
  }
}
