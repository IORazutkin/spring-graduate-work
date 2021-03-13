package com.iorazutkin.graduatework.api.task;

import com.iorazutkin.graduatework.dto.QuestionDto;
import com.iorazutkin.graduatework.entity.Student;
import com.iorazutkin.graduatework.entity.User;
import com.iorazutkin.graduatework.entity.UserPK;
import com.iorazutkin.graduatework.entity.task.*;
import com.iorazutkin.graduatework.repo.StudentRepo;
import com.iorazutkin.graduatework.repo.UserRepo;
import com.iorazutkin.graduatework.repo.task.AnswerRepo;
import com.iorazutkin.graduatework.repo.task.QuestionRepo;
import com.iorazutkin.graduatework.repo.task.TaskRepo;
import com.iorazutkin.graduatework.repo.task.TaskResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/question")
public class QuestionController {
  @Autowired
  private QuestionRepo questionRepo;

  @Autowired
  private TaskRepo taskRepo;

  @Autowired
  private TaskResultRepo taskResultRepo;

  @Autowired
  private StudentRepo studentRepo;

  @Autowired
  private AnswerRepo answerRepo;

  @Autowired
  private UserRepo userRepo;

  @GetMapping("/teacher/{practiceId}/{studentId}")
  public ResponseEntity<List<QuestionDto>> teacherCheckResult (
    @AuthenticationPrincipal User user,
    @PathVariable Long practiceId,
    @PathVariable Long studentId
  ) {
    Task task = taskRepo.findById(practiceId).get();

    if (task == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!task.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    User studentUser = userRepo.findById(studentId).get();
    Student student = studentRepo.findById(new UserPK(studentUser)).get();
    TaskResult taskResult = taskResultRepo.getTopByTaskAndStudent(task, student);

    List<QuestionDto> questionList =
      questionRepo
        .findAllByTask(task)
        .stream()
        .map(QuestionDto::new)
        .collect(Collectors.toList());

    if (taskResult != null) {
      List<Answer> answerList =
        answerRepo.findAllById_TaskResult(taskResult);

      questionList.forEach(item -> {
        answerList.stream().filter(answer ->
          answer.getId().getQuestion().equals(item.getQuestion())
        ).findFirst().ifPresent(answer -> item.setAnswer(answer.getAnswer()));
      });
    }

    return new ResponseEntity<>(questionList, null, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<List<QuestionDto>> findAllByTask (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    HttpServletResponse response
  ) {
    Task task = taskRepo.findById(id).get();

    if (task == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    List<QuestionDto> questionList =
      questionRepo
        .findAllByTask(task)
        .stream()
        .map(QuestionDto::new)
        .collect(Collectors.toList());

    if (!task.getFinishDate().isAfter(LocalDate.now())) {
      return new ResponseEntity<>(questionList, null, HttpStatus.OK);
    }

    if (user.getRole().getName().equals("STUDENT")) {
      Student student = studentRepo.findById(new UserPK(user)).get();
      TaskResult taskResult = taskResultRepo.getTopByTaskAndStudent(task, student);

      if (taskResult == null) {
        taskResult = new TaskResult(student, task);
        taskResultRepo.save(taskResult);

        Cookie cookie = new Cookie("task", task.getId().toString());
        cookie.setMaxAge(60 * 5);
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
      } else {
        List<Answer> answerList =
          answerRepo.findAllById_TaskResult(taskResult);

        questionList.forEach(item -> {
          answerList.stream().filter(answer ->
            answer.getId().getQuestion().equals(item.getQuestion())
          ).findFirst().ifPresent(answer -> item.setAnswer(answer.getAnswer()));
        });
      }
    } else if (!task.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(questionList, null, HttpStatus.OK);
  }

  @PostMapping("{id}")
  public ResponseEntity<Question> addQuestion (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    @RequestBody Question question
  ) {
    Task task = taskRepo.findById(id).get();

    if (task == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!task.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    question.setTask(task);
    question = questionRepo.save(question);

    return new ResponseEntity<>(question, null, HttpStatus.OK);
  }

  @PatchMapping("{id}")
  public ResponseEntity<Question> editQuestion (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    @RequestBody Question question
  ) {
    Question questionFromDb = questionRepo.findById(id).get();

    if (questionFromDb == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!questionFromDb.getTask().getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    questionFromDb.setQuestion(question.getQuestion());
    questionFromDb.setAnswers(question.getAnswers());
    questionFromDb.setRightAnswer(question.getRightAnswer());

    question = questionRepo.save(questionFromDb);

    return new ResponseEntity<>(question, null, HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Object> deleteQuestion (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Question question = questionRepo.findById(id).get();

    if (question == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!question.getTask().getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    questionRepo.delete(question);

    return new ResponseEntity<>(null, null, HttpStatus.OK);
  }
}
