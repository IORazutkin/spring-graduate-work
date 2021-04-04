package com.iorazutkin.graduatework.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.practice.PracticeAuthView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Teacher {
  @EmbeddedId
  @JsonView(PracticeAuthView.class)
  private UserPK user;

  @Column
  private String post;
}
