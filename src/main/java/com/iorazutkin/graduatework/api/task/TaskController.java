package com.iorazutkin.graduatework.api.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.dto.TaskDto;
import com.iorazutkin.graduatework.dto.TaskWithResultDto;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import com.iorazutkin.graduatework.entity.task.Theory;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.exception.BadRequestException;
import com.iorazutkin.graduatework.repo.task.QuestionRepo;
import com.iorazutkin.graduatework.repo.task.TaskRepo;
import com.iorazutkin.graduatework.service.FileService;
import com.iorazutkin.graduatework.service.practice.PracticeService;
import com.iorazutkin.graduatework.service.task.TaskResultService;
import com.iorazutkin.graduatework.service.task.TaskService;
import com.iorazutkin.graduatework.view.task.TaskView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {
  private final TaskRepo taskRepo;
  private final FileService fileService;
  private final PracticeService practiceService;
  private final TaskService taskService;
  private final TaskResultService taskResultService;
  private final QuestionRepo questionRepo;

  @GetMapping("/teacher/{id}")
  @JsonView(TaskView.class)
  public ResponseEntity<List<Task>> findAllByPracticeId (
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(taskService.findAllByPracticeId(id));
  }

  @GetMapping("{id}")
  @JsonView(TaskView.class)
  public ResponseEntity<List<TaskWithResultDto>> findAllByPractice (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    List<Task> taskList = taskService.findAllByPracticeId(id);
    List<TaskWithResultDto> result = new ArrayList<>();

    taskList.forEach(item -> {
      TaskResult taskResult = taskResultService.findByTaskAndUser(item, auth);
      result.add(new TaskWithResultDto(
        item,
        taskResult == null ? null : taskResult.getScore(),
        questionRepo.countAllByTask(item)
      ));
    });

    return ResponseEntity.ok(result);
  }

  @GetMapping("/teacher/{practiceId}/{userId}")
  @JsonView(TaskView.class)
  public ResponseEntity<List<TaskWithResultDto>> findAllWithResult (
    @PathVariable Long practiceId,
    @PathVariable Long userId
  ) {
    List<Task> taskList = taskService.findAllByPracticeId(practiceId);
    List<TaskWithResultDto> result = new ArrayList<>();

    taskList.forEach(item -> {
      TaskResult taskResult = taskResultService.findByTaskAndUserId(item, userId);
      result.add(new TaskWithResultDto(
        item,
        taskResult == null ? null : taskResult.getScore(),
        questionRepo.countAllByTask(item)
      ));
    });

    return ResponseEntity.ok(result);
  }

  @PostMapping
  @JsonView(TaskView.class)
  public ResponseEntity<Task> addTask (
    @AuthenticationPrincipal User auth,
    @Validated @ModelAttribute TaskDto taskDto
  ) throws IOException {
    Practice practice = practiceService.findById(taskDto.getPracticeId(), auth);

    String filePath = fileService.loadFile(taskDto.getFile(), "theory");
    Task task = taskDto.toTask();
    task.setPractice(practice);
    task.setTheory(new Theory(filePath));

    return new ResponseEntity<>(taskRepo.save(task), HttpStatus.CREATED);
  }

  @PatchMapping("{id}")
  @JsonView(TaskView.class)
  public ResponseEntity<Task> updateTask (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    @Validated @ModelAttribute TaskDto taskDto
  ) throws IOException {
    Task task = taskService.findById(id, auth);
    taskDto.copyNotNull(task);

    try {
      String filePath = fileService.loadFile(taskDto.getFile(), "theory");
      fileService.removeFile("theory", task.getTheory().getContentUrl());
      task.getTheory().setContentUrl(filePath);
    } catch (BadRequestException ignored) {}

    return ResponseEntity.ok(taskRepo.save(task));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Boolean> deleteTask (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    Task task = taskService.findById(id, auth);
    task.setDeleted(true);
    taskRepo.save(task);

    return ResponseEntity.ok(true);
  }
}
