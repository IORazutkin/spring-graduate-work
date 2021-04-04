package com.iorazutkin.graduatework.api.task;

import com.iorazutkin.graduatework.dto.AnswerDto;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.task.Answer;
import com.iorazutkin.graduatework.entity.task.Question;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import com.iorazutkin.graduatework.repo.task.AnswerRepo;
import com.iorazutkin.graduatework.repo.task.TaskResultRepo;
import com.iorazutkin.graduatework.service.task.AnswerService;
import com.iorazutkin.graduatework.service.task.QuestionService;
import com.iorazutkin.graduatework.service.task.TaskResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerController {
  private final AnswerRepo answerRepo;
  private final TaskResultService taskResultService;
  private final QuestionService questionService;
  private final AnswerService answerService;
  private final TaskResultRepo taskResultRepo;

  @GetMapping("{id}")
  public ResponseEntity<List<Answer>> findAllByTask (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(
      answerRepo.findAllById_TaskResult(taskResultService.findByTaskIdAndUser(id, auth))
    );
  }

  @PostMapping("{id}")
  public ResponseEntity<Answer> addAnswer (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    @RequestBody AnswerDto answerDto
  ) {
    TaskResult taskResult = taskResultService.findByTaskIdAndUser(id, auth);
    Question question = questionService.findById(answerDto.getQuestionId());

    if (question.isDateOfOut()) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    Answer answer = answerService.findById(taskResult, question);

    if (answer != null && answer.getAnswer().equals(question.getRightAnswer())) {
      taskResult.decScore();
    }

    if (answerDto.getSelectAnswer().equals(question.getRightAnswer() + 1)) {
      taskResult.incScore();
    }

    taskResultRepo.save(taskResult);

    return ResponseEntity.ok(answerService.createNew(taskResult, question, answerDto));
  }
}
