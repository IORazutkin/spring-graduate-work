package com.iorazutkin.graduatework.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class TaskDto implements Serializable {
  Long practiceId;
  String title;
  MultipartFile file;
  String finishDate;
}
