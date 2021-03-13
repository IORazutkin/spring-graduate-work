package com.iorazutkin.graduatework.entity.task;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Theory {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column
  private String contentUrl;

  public Theory (String contentUrl) {
    this.contentUrl = contentUrl;
  }

  public Theory () {
  }
}
