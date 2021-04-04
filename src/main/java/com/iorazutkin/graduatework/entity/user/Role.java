package com.iorazutkin.graduatework.entity.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.user.UserView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @JsonView(UserView.class)
  @Column
  private String name;
}
