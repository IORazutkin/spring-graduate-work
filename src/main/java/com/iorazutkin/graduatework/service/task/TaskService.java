package com.iorazutkin.graduatework.service.task;

import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.exception.ForbiddenException;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.task.TaskRepo;
import com.iorazutkin.graduatework.service.practice.PracticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {
  private final TaskRepo taskRepo;
  private final PracticeService practiceService;

  public Task findById (Long id) {
    Optional<Task> taskOptional = taskRepo.findById(id);

    if (taskOptional.isPresent()) {
      return taskOptional.get();
    }

    throw new NotFoundException();
  }

  public Task findById (Long id, User user) {
    Task task = findById(id);

    if (task.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return task;
    }

    throw new ForbiddenException();
  }

  public List<Task> findAllByPracticeId (Long id) {
    Practice practice = practiceService.findById(id);
    return taskRepo.findAllByPracticeAndDeletedIsFalse(practice);
  }
}
