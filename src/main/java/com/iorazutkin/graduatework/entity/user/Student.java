package com.iorazutkin.graduatework.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.view.practice.StudentPracticeListView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Student {
  @EmbeddedId
  @JsonView(StudentPracticeListView.class)
  private UserPK user;

  @Column
  private Character sex;

  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;
}
