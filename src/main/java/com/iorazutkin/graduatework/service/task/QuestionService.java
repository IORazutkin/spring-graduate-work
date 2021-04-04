package com.iorazutkin.graduatework.service.task;

import com.iorazutkin.graduatework.dto.QuestionDto;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.task.Answer;
import com.iorazutkin.graduatework.entity.task.Question;
import com.iorazutkin.graduatework.entity.task.Task;
import com.iorazutkin.graduatework.entity.task.TaskResult;
import com.iorazutkin.graduatework.exception.ForbiddenException;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.task.AnswerRepo;
import com.iorazutkin.graduatework.repo.task.QuestionRepo;
import com.iorazutkin.graduatework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
  private final QuestionRepo questionRepo;
  private final AnswerRepo answerRepo;
  private final UserService userService;
  private final TaskService taskService;
  private final TaskResultService taskResultService;

  public Question findById (Long id) {
    Optional<Question> questionOptional = questionRepo.findById(id);

    if (questionOptional.isPresent()) {
      return questionOptional.get();
    }

    throw new NotFoundException();
  }

  public Question findById (Long id, User user) {
    Question question = findById(id);

    if (!question.getTask().getPractice().getTeacher().getUser().getUser().equals(user)) {
      throw new ForbiddenException();
    }

    return question;
  }

  public List<QuestionDto> findByTaskIdAndUserId (Long taskId, Long userId) {
    Task task = taskService.findById(taskId);
    User user = userService.findById(userId);

    List<QuestionDto> result = findDtoByTask(task);
    return setAnswers(result, taskResultService.findByTaskIdAndUser(taskId, user));
  }

  public List<QuestionDto> findDtoByTask (Task task) {
    return questionRepo
      .findAllByTask(task)
      .stream()
      .map(QuestionDto::new)
      .collect(Collectors.toList());
  }

  public void removeRightAnswer (List<QuestionDto> list) {
    list.forEach(item -> {
      item.getQuestion().setRightAnswer(null);
    });
  }

  public List<QuestionDto> setAnswers (List<QuestionDto> questionList, TaskResult taskResult) {
    if (taskResult != null) {
      List<Answer> answerList =
        answerRepo.findAllById_TaskResult(taskResult);

      questionList.forEach(item -> {
        answerList.stream().filter(answer ->
          answer.getId().getQuestion().equals(item.getQuestion())
        ).findFirst().ifPresent(answer -> item.setAnswer(answer.getAnswer()));
      });
    }

    return questionList;
  }
}
