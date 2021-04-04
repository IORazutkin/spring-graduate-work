package com.iorazutkin.graduatework.repo.task;

import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskResultRepo extends JpaRepository<TaskResult, Long> {
  List<TaskResult> findAllByStudent (Student student);
  TaskResult getTopByTaskAndStudent (Task task, Student student);
}
