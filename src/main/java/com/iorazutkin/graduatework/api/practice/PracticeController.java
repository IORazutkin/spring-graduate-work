package com.iorazutkin.graduatework.api.practice;

import com.iorazutkin.graduatework.dto.PracticeDto;
import com.iorazutkin.graduatework.entity.Student;
import com.iorazutkin.graduatework.entity.Teacher;
import com.iorazutkin.graduatework.entity.User;
import com.iorazutkin.graduatework.entity.UserPK;
import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.StudentPractice;
import com.iorazutkin.graduatework.entity.practice.Work;
import com.iorazutkin.graduatework.repo.practice.PracticeRepo;
import com.iorazutkin.graduatework.repo.practice.StudentPracticeRepo;
import com.iorazutkin.graduatework.repo.StudentRepo;
import com.iorazutkin.graduatework.repo.TeacherRepo;
import com.iorazutkin.graduatework.repo.practice.ThemeRepo;
import com.iorazutkin.graduatework.repo.task.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/practice")
public class PracticeController {
  @Autowired
  private PracticeRepo practiceRepo;

  @Autowired
  private TeacherRepo teacherRepo;

  @Autowired
  private ThemeRepo themeRepo;

  @Autowired
  private TaskRepo taskRepo;

  @Autowired
  private StudentRepo studentRepo;

  @Autowired
  private StudentPracticeRepo studentPracticeRepo;

  @Value("${upload.path}")
  private String uploadPath;

  @GetMapping
  public ResponseEntity<List<Practice>> findAllTeacherPractices (@AuthenticationPrincipal User user) {
    Teacher teacher = teacherRepo.findById(new UserPK(user)).get();
    List<Practice> practices = practiceRepo.findAllByTeacher(teacher);
    return new ResponseEntity<>(practices, null, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<Practice> findById (@AuthenticationPrincipal User user,
                                            @PathVariable Long id) {
    Practice practice = practiceRepo.findById(id).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    return new ResponseEntity<>(practice, null, HttpStatus.OK);
  }

  @GetMapping("/dto/{id}")
  public ResponseEntity<PracticeDto> findDtoById (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Practice practice = practiceRepo.findById(id).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    List<StudentPractice> list = studentPracticeRepo.findAllById_Practice(practice);

    List<Group> groups = list.stream()
      .map((item) -> item.getId().getStudent().getGroup())
      .distinct().collect(Collectors.toList());

    PracticeDto practiceDto = new PracticeDto();
    practiceDto.setFinishDate(practice.getFinishDate());
    practiceDto.setTitle(practice.getTitle());
    practiceDto.setGroupList(groups);

    return new ResponseEntity<>(practiceDto, null, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Practice> createPractice (@AuthenticationPrincipal User user,
                                                  @RequestBody PracticeDto practiceDto
  ) {
    Teacher teacher = teacherRepo.findById(new UserPK(user)).get();
    Practice practice = new Practice(practiceDto.getTitle(), practiceDto.getFinishDate(), teacher);
    practice = practiceRepo.save(practice);

    for (Group group: practiceDto.getGroupList()) {
      List<Student> students = studentRepo.findAllByGroup(group);

      for (Student student: students) {
        StudentPractice studentPractice = new StudentPractice(student, practice);
        studentPracticeRepo.save(studentPractice);
      }
    }

    return new ResponseEntity<>(practice, null, HttpStatus.OK);
  }

  @PutMapping("{id}")
  public ResponseEntity<Practice> updatePractice (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    @RequestBody PracticeDto practiceDto
  ) {
    Practice practice = practiceRepo.findById(id).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    List<StudentPractice> studentPractices = studentPracticeRepo.findAllById_Practice(practice);
    List<Group> groups = practiceDto.getGroupList();

    studentPracticeRepo.deleteAll(
      studentPractices.stream()
        .filter(item -> !groups.contains(item.getId().getStudent().getGroup()))
        .collect(Collectors.toList())
    );

    for (Group group: groups) {
      if (
        studentPractices.stream()
          .anyMatch(item -> item.getId().getStudent().getGroup().equals(group))
      ) {
        continue;
      }

      List<Student> students = studentRepo.findAllByGroup(group);

      for (Student student: students) {
        StudentPractice studentPractice = new StudentPractice(student, practice);
        studentPracticeRepo.save(studentPractice);
      }
    }

    practice.setFinishDate(practiceDto.getFinishDate());
    practice.setTitle(practiceDto.getTitle());

    practice = practiceRepo.save(practice);

    return new ResponseEntity<>(practice, null, HttpStatus.OK);
  }

  @PutMapping("{practiceId}/file")
  public ResponseEntity<Practice> updateFile (
    @AuthenticationPrincipal User user,
    @RequestParam("file")MultipartFile file,
    @PathVariable Long practiceId
  ) throws IOException {
    Practice practice = practiceRepo.findById(practiceId).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    if (file != null) {
      File uploadDir = new File(uploadPath);

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFilename = uuidFile + "." + file.getOriginalFilename();

      file.transferTo(new File(uploadPath + "/template/" + resultFilename));

      practice.setTemplate(resultFilename);
      practice = practiceRepo.save(practice);
    }

    return new ResponseEntity<>(practice, null, HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Object> deletePractice (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Practice practice = practiceRepo.findById(id).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    studentPracticeRepo.deleteAll(
      studentPracticeRepo.findAllById_Practice(practice)
    );

    taskRepo.deleteAll(
      taskRepo.findAllByPractice(practice)
    );

    themeRepo.deleteAll(
      themeRepo.findAllByPractice(practice)
    );

    practiceRepo.delete(practice);

    return new ResponseEntity<>(null, null, HttpStatus.OK);
  }
}
