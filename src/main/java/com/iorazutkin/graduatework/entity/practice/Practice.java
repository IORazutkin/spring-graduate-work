package com.iorazutkin.graduatework.entity.practice;

import com.iorazutkin.graduatework.entity.Teacher;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Practice {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String title;

  @Column
  private LocalDate finishDate;

  @ManyToOne
  @JoinColumn(name = "teacher_id")
  private Teacher teacher;

  @Column
  private String template;

  public Practice () {}

  public Practice (String title, LocalDate finishDate, Teacher teacher) {
    this.title = title;
    this.finishDate = finishDate;
    this.teacher = teacher;
    this.template = null;
  }
}
