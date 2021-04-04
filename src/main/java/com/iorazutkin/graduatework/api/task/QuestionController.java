package com.iorazutkin.graduatework.api.task;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.dto.QuestionDto;
import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.task.Question;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import com.iorazutkin.graduatework.repo.task.QuestionRepo;
import com.iorazutkin.graduatework.repo.task.TaskResultRepo;
import com.iorazutkin.graduatework.service.task.QuestionService;
import com.iorazutkin.graduatework.service.user.StudentService;
import com.iorazutkin.graduatework.service.task.TaskService;
import com.iorazutkin.graduatework.view.task.QuestionTestView;
import com.iorazutkin.graduatework.view.task.QuestionView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionController {
  private final QuestionRepo questionRepo;
  private final TaskResultRepo taskResultRepo;
  private final QuestionService questionService;
  private final TaskService taskService;
  private final StudentService studentService;

  @GetMapping("/teacher/{taskId}/{userId}")
  @JsonView(QuestionView.class)
  public ResponseEntity<List<QuestionDto>> teacherCheckResult (
    @PathVariable Long taskId,
    @PathVariable Long userId
  ) {
    return ResponseEntity.ok(questionService.findByTaskIdAndUserId(taskId, userId));
  }

  @GetMapping("{id}")
  @JsonView(QuestionView.class)
  public ResponseEntity<List<QuestionDto>> findAllByTask (
    @CookieValue(name = "task", required = false) Cookie reqCookie,
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    HttpServletResponse response
  ) {
    Task task = taskService.findById(id);
    List<QuestionDto> questionList = questionService.findDtoByTask(task);

    if (auth.isStudent()) {
      Student student = studentService.findByUser(auth);
      TaskResult taskResult = taskResultRepo.getTopByTaskAndStudent(task, student);
      questionList = questionService.setAnswers(questionList, taskResult);

      if (!task.isDateOfOut()) {
        return ResponseEntity.ok(questionList);
      }

      if (reqCookie != null || taskResult == null) {
        questionService.removeRightAnswer(questionList);
      }

      if (taskResult == null) {
        taskResult = new TaskResult(student, task);
        taskResultRepo.save(taskResult);

        Cookie cookie = new Cookie("task", task.getId().toString());
        cookie.setMaxAge(60 * questionList.size());
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
      }
    } else if (!task.getPractice().getTeacher().getUser().getUser().equals(auth)) {
      return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    return ResponseEntity.ok(questionList);
  }

  @PostMapping("{id}")
  @JsonView(QuestionView.class)
  public ResponseEntity<QuestionDto> addQuestion (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    @RequestBody Question question
  ) {
    Task task = taskService.findById(id, auth);
    question.setTask(task);

    return ResponseEntity.ok(new QuestionDto(questionRepo.save(question)));
  }

  @PatchMapping("{id}")
  @JsonView(QuestionView.class)
  public ResponseEntity<QuestionDto> editQuestion (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    @RequestBody Question question
  ) {
    Question questionFromDb = questionService.findById(id, auth);

    BeanUtils.copyProperties(question, questionFromDb, "id", "task");
    return ResponseEntity.ok(new QuestionDto(questionRepo.save(questionFromDb)));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Boolean> deleteQuestion (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    Question question = questionService.findById(id, auth);
    questionRepo.delete(question);

    return ResponseEntity.ok(true);
  }
}
