package com.iorazutkin.graduatework.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Teacher {
  @EmbeddedId
  private UserPK user;

  @Column
  private String post;
}
