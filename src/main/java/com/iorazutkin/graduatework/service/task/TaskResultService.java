package com.iorazutkin.graduatework.service.task;

import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.task.TaskResultRepo;
import com.iorazutkin.graduatework.service.user.StudentService;
import com.iorazutkin.graduatework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskResultService {
  private final TaskResultRepo taskResultRepo;
  private final TaskService taskService;
  private final StudentService studentService;
  private final UserService userService;

  public TaskResult findByTaskAndUser (Task task, User user) {
    Student student = studentService.findByUser(user);

    return taskResultRepo.getTopByTaskAndStudent(task, student);
  }

  public TaskResult findByTaskAndUserId (Task task, Long userId) {
    User user = userService.findById(userId);
    return findByTaskAndUser(task, user);
  }

  public TaskResult findByTaskIdAndUser (Long taskId, User user) {
    Task task = taskService.findById(taskId);
    return findByTaskAndUser(task, user);
  }

  public TaskResult findByTaskIdAndUserId (Long taskId, Long userId) {
    User user = userService.findById(userId);
    return findByTaskIdAndUser(taskId, user);
  }
}
