package com.iorazutkin.graduatework.dto;

import lombok.Data;

@Data
public class UpdateScoreDto {
  Long practiceId;
  Long studentId;
  String comment;
  Integer score;
}
