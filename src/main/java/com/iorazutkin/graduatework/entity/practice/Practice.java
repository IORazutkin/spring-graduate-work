package com.iorazutkin.graduatework.entity.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.user.Teacher;
import com.iorazutkin.graduatework.view.practice.PracticeAuthView;
import com.iorazutkin.graduatework.view.practice.PracticeView;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Practice {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView({PracticeView.class, PracticeAuthView.class})
  private Long id;

  @Column
  @JsonView({PracticeView.class, PracticeAuthView.class})
  private String title;

  @Column
  @JsonView({PracticeView.class, PracticeAuthView.class})
  private LocalDate finishDate;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  @JsonView({PracticeView.class, PracticeAuthView.class})
  private Teacher teacher;

  @Column
  @JsonView({PracticeView.class, PracticeAuthView.class})
  private String template;

  @Column
  private Boolean deleted = false;

  public Practice () {}
}
