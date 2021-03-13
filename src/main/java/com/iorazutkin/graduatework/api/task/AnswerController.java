package com.iorazutkin.graduatework.api.task;

import com.iorazutkin.graduatework.dto.AnswerDto;
import com.iorazutkin.graduatework.entity.Student;
import com.iorazutkin.graduatework.entity.User;
import com.iorazutkin.graduatework.entity.UserPK;
import com.iorazutkin.graduatework.entity.task.*;
import com.iorazutkin.graduatework.repo.StudentRepo;
import com.iorazutkin.graduatework.repo.task.AnswerRepo;
import com.iorazutkin.graduatework.repo.task.QuestionRepo;
import com.iorazutkin.graduatework.repo.task.TaskRepo;
import com.iorazutkin.graduatework.repo.task.TaskResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {
  @Autowired
  private AnswerRepo answerRepo;

  @Autowired
  private StudentRepo studentRepo;

  @Autowired
  private QuestionRepo questionRepo;

  @Autowired
  private TaskRepo taskRepo;

  @Autowired
  private TaskResultRepo taskResultRepo;

  @GetMapping("{id}")
  public ResponseEntity<List<Answer>> findAllByTask (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Task task = taskRepo.findById(id).get();
    Student student = studentRepo.findById(new UserPK(user)).get();
    TaskResult taskResult = taskResultRepo.getTopByTaskAndStudent(task, student);

    if (taskResult == null) {
      return new ResponseEntity<>(null, null, HttpStatus.OK);
    }

    List<Answer> answers = answerRepo.findAllById_TaskResult(taskResult);

    return new ResponseEntity<>(answers, null, HttpStatus.OK);
  }

  @PostMapping("{id}")
  public ResponseEntity<Answer> addAnswer (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    @RequestBody AnswerDto answerDto
    ) {
    Student student = studentRepo.findById(new UserPK(user)).get();
    Task task = taskRepo.findById(id).get();
    Question question = questionRepo.findById(answerDto.getQuestionId()).get();
    TaskResult taskResult = taskResultRepo.getTopByTaskAndStudent(task, student);

    if (question.getTask().getFinishDate().compareTo(LocalDate.now()) < 0) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    Answer answer = new Answer();
    answer.setAnswer(answerDto.getSelectAnswer());
    answer.setId(new AnswerPK(taskResult, question));
    answer = answerRepo.save(answer);

    return new ResponseEntity<>(answer, null, HttpStatus.OK);
  }
}
