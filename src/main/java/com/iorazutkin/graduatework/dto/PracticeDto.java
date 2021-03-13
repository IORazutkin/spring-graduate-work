package com.iorazutkin.graduatework.dto;

import com.iorazutkin.graduatework.entity.institute.Group;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PracticeDto {
  String title;
  LocalDate finishDate;
  List<Group> groupList;
}
