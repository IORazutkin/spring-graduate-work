package com.iorazutkin.graduatework.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.practice.PracticeAuthView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeListView;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Data
@Embeddable
public class UserPK implements Serializable {
  @OneToOne
  @JoinColumn(name = "user_id")
  @JsonView({StudentPracticeListView.class, PracticeAuthView.class})
  private User user;

  public UserPK () {}

  public UserPK (User user) {
    this.user = user;
  }
}
