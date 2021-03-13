package com.iorazutkin.graduatework.entity.institute;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Institute {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String title;
}
