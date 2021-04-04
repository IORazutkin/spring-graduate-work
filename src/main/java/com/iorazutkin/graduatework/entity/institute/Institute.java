package com.iorazutkin.graduatework.entity.institute;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.view.institute.InstituteView;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Institute {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @JsonView(InstituteView.class)
  private Long id;

  @Column
  @JsonView(InstituteView.class)
  private String title;
}
