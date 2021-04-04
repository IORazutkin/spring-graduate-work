package com.iorazutkin.graduatework.dto;

import com.iorazutkin.graduatework.entity.task.Task;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TaskDto implements Serializable {
  Long practiceId;
  String title;
  MultipartFile file;
  String finishDate;

  public Task toTask () {
    Task task = new Task();
    task.setTitle(this.title);
    task.setFinishDate(LocalDate.parse(this.finishDate));

    return task;
  }

  public void copyNotNull (Task task) {
    if (this.title != null) {
      task.setTitle(this.title);
    }

    if (this.finishDate != null) {
      task.setFinishDate(LocalDate.parse(this.finishDate));
    }
  }
}
