package com.iorazutkin.graduatework.repo.task;

import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task, Long> {
  List<Task> findAllByPracticeAndDeletedIsFalse (Practice practice);
}
