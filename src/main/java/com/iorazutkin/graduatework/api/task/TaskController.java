package com.iorazutkin.graduatework.api.task;

import com.iorazutkin.graduatework.dto.TaskDto;
import com.iorazutkin.graduatework.entity.User;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.entity.task.Theory;
import com.iorazutkin.graduatework.repo.practice.PracticeRepo;
import com.iorazutkin.graduatework.repo.task.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {
  @Autowired
  private TaskRepo taskRepo;

  @Autowired
  private PracticeRepo practiceRepo;

  @Value("${upload.path}")
  private String uploadPath;

  @GetMapping("{id}")
  public ResponseEntity<List<Task>> findAllByPractice (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Practice practice = practiceRepo.findById(id).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(taskRepo.findAllByPractice(practice), null, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Task> addTask (
    @AuthenticationPrincipal User user,
    @RequestParam Long practiceId,
    @RequestParam String title,
    @RequestParam("file") MultipartFile file,
    @RequestParam String finishDate
    ) throws IOException {
    Practice practice = practiceRepo.findById(practiceId).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    if (file != null) {
      File uploadDir = new File(uploadPath);

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFilename = uuidFile + "." + file.getOriginalFilename();

      file.transferTo(new File(uploadPath + "/theory/" + resultFilename));

      Task task = new Task();
      task.setFinishDate(LocalDate.parse(finishDate));
      task.setPractice(practice);
      task.setTheory(new Theory(resultFilename));
      task.setTitle(title);

      return new ResponseEntity<>(taskRepo.save(task), null, HttpStatus.CREATED);
    }

    return new ResponseEntity<>(null, null, HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @PatchMapping("{id}")
  public ResponseEntity<Task> updateTask (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    @Validated @ModelAttribute TaskDto task
  ) throws IOException {
    Task taskFromDb = taskRepo.findById(id).get();

    if (taskFromDb == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!taskFromDb.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    if (task.getTitle() != null) {
      taskFromDb.setTitle(task.getTitle());
    }
    if (task.getFinishDate() != null) {
      taskFromDb.setFinishDate(LocalDate.parse(task.getFinishDate()));
    }
    if (task.getFile() != null) {
      File uploadDir = new File(uploadPath);

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFilename = uuidFile + "." + task.getFile().getOriginalFilename();

      task.getFile().transferTo(new File(uploadPath + "/theory/" + resultFilename));

      File deleteFile = new File(uploadPath + "/" + taskFromDb.getTheory().getContentUrl());
      deleteFile.delete();
      taskFromDb.getTheory().setContentUrl(resultFilename);
    }

    taskFromDb = taskRepo.save(taskFromDb);

    return new ResponseEntity<>(taskFromDb, null, HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Object> deleteTask (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Task task = taskRepo.findById(id).get();

    if (task == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    if (!task.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    taskRepo.delete(task);

    return new ResponseEntity<>(null, null, HttpStatus.OK);
  }
}
