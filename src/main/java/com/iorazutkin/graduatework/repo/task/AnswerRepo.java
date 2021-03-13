package com.iorazutkin.graduatework.repo.task;

import com.iorazutkin.graduatework.entity.task.Answer;
import com.iorazutkin.graduatework.entity.task.AnswerPK;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepo extends JpaRepository<Answer, AnswerPK> {
  List<Answer> findAllById_TaskResult (TaskResult taskResult);
}
