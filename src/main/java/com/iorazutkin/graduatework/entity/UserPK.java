package com.iorazutkin.graduatework.entity;

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
  private User user;

  public UserPK () {}

  public UserPK (User user) {
    this.user = user;
  }
}
