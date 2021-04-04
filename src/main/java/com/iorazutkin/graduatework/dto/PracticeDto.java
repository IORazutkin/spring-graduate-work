package com.iorazutkin.graduatework.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.institute.Course;
import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.view.practice.PracticeView;
import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.time.LocalDate;
import java.util.List;

@Data
public class PracticeDto {
  @JsonView(PracticeView.class)
  String title;
  @JsonView(PracticeView.class)
  LocalDate finishDate;
  @JsonView(PracticeView.class)
  List<Group> groupList;
  @JsonView(PracticeView.class)
  Course course;

  public PracticeDto () {}

  public PracticeDto (Practice practice) {
    this.title = practice.getTitle();
    this.finishDate = practice.getFinishDate();
  }

  public PracticeDto (Practice practice, List<Group> groupList) {
    this(practice);
    this.groupList = groupList;
    this.course = groupList.get(0).getCourse();
  }

  public Practice toPractice () {
    Practice practice = new Practice();
    practice.setTitle(this.title);
    practice.setFinishDate(this.finishDate);

    return practice;
  }

  public void copyTo (Practice practice) {
    practice.setTitle(this.title);
    practice.setFinishDate(this.finishDate);
  }
}
