package com.iorazutkin.graduatework.repo.task;

import com.iorazutkin.graduatework.entity.task.Question;
import com.iorazutkin.graduatework.entity.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepo extends JpaRepository<Question, Long> {
  List<Question> findAllByTask (Task task);
  Integer countAllByTask (Task task);
}
