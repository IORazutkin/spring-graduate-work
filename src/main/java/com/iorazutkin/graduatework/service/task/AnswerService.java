package com.iorazutkin.graduatework.service.task;

import com.iorazutkin.graduatework.dto.AnswerDto;
import com.iorazutkin.graduatework.entity.task.Answer;
import com.iorazutkin.graduatework.entity.task.AnswerPK;
import com.iorazutkin.graduatework.entity.task.Question;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import com.iorazutkin.graduatework.repo.task.AnswerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
  private final AnswerRepo answerRepo;

  public Answer findById (TaskResult taskResult, Question question) {
    Optional<Answer> answerOptional = answerRepo.findById(new AnswerPK(taskResult, question));
    return answerOptional.orElse(null);
  }

  public Answer createNew (TaskResult taskResult, Question question, AnswerDto answerDto) {
    Answer answer = new Answer();
    answer.setAnswer(answerDto.getSelectAnswer());
    answer.setId(new AnswerPK(taskResult, question));

    return answerRepo.save(answer);
  }
}
