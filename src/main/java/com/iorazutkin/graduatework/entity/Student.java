package com.iorazutkin.graduatework.entity;

import com.iorazutkin.graduatework.entity.institute.Group;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Student {
  @EmbeddedId
  private UserPK user;

  @Column
  private Character sex;

  @ManyToOne
  @JoinColumn(name = "group_id")
  private Group group;
}
