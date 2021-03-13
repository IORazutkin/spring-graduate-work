package com.iorazutkin.graduatework.api.practice;

import com.iorazutkin.graduatework.dto.UpdateScoreDto;
import com.iorazutkin.graduatework.entity.Student;
import com.iorazutkin.graduatework.entity.User;
import com.iorazutkin.graduatework.entity.UserPK;
import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.StudentPractice;
import com.iorazutkin.graduatework.entity.practice.StudentPracticePK;
import com.iorazutkin.graduatework.entity.practice.Work;
import com.iorazutkin.graduatework.repo.StudentRepo;
import com.iorazutkin.graduatework.repo.UserRepo;
import com.iorazutkin.graduatework.repo.practice.PracticeRepo;
import com.iorazutkin.graduatework.repo.practice.StudentPracticeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/student-practice")
public class StudentPracticeController {
  @Autowired
  private StudentPracticeRepo studentPracticeRepo;

  @Autowired
  private StudentRepo studentRepo;

  @Autowired
  private PracticeRepo practiceRepo;

  @Autowired
  private UserRepo userRepo;

  @Value("${upload.path}")
  private String uploadPath;

  @GetMapping
  public ResponseEntity<List<StudentPractice>> findAll (
    @AuthenticationPrincipal User user
  ) {
    Student student = studentRepo.findById(new UserPK(user)).get();

    List<StudentPractice> studentPractices = studentPracticeRepo.findAllById_Student(student);

    return new ResponseEntity<>(studentPractices, null, HttpStatus.OK);
  }

  @GetMapping("{id}")
  public ResponseEntity<StudentPractice> getOne (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Student student = studentRepo.findById(new UserPK(user)).get();
    Practice practice = practiceRepo.findById(id).get();

    StudentPractice studentPractice = studentPracticeRepo.findById(new StudentPracticePK(student, practice)).get();

    if (studentPractice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(studentPractice, null, HttpStatus.OK);
  }

  @GetMapping("/teacher")
  public ResponseEntity<StudentPractice> findByPracticeAndStudent (
    @AuthenticationPrincipal User user,
    @RequestParam(name = "practice_id") Long practiceId,
    @RequestParam(name = "student_id") Long studentId
  ) {
    Practice practice = practiceRepo.findById(practiceId).get();

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    User studentUser = userRepo.findById(studentId).get();

    if (studentUser == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    Student student = studentRepo.findById(new UserPK(studentUser)).get();

    if (student == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    StudentPractice studentPractice = studentPracticeRepo.findById(new StudentPracticePK(student, practice)).get();

    if (studentPractice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(studentPractice, null, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Map<String, List<StudentPractice>>> findByPractice (
    @RequestBody Practice practice
  ) {
    List<StudentPractice> studentPractices = studentPracticeRepo.findAllById_Practice(practice);
    Map<String, List<StudentPractice>> map = new HashMap<>();

    studentPractices.forEach((studentPractice) -> {
      Group group = studentPractice.getId().getStudent().getGroup();

      if (map.containsKey(group.getNum())) {
        map.get(group.getNum()).add(studentPractice);
      } else {
        map.put(group.getNum(), Arrays.asList(studentPractice));
      }
    });

    return new ResponseEntity<>(map, null, HttpStatus.OK);
  }

  @PutMapping("{practiceId}")
  public ResponseEntity<StudentPractice> updateWorkFile (
    @AuthenticationPrincipal User user,
    @PathVariable Long practiceId,
    @RequestParam("file") MultipartFile file
  ) throws IOException {
    Student student = studentRepo.findById(new UserPK(user)).get();
    Practice practice = practiceRepo.findById(practiceId).get();

    StudentPractice studentPractice = studentPracticeRepo.findById(new StudentPracticePK(student, practice)).get();

    if (file != null) {
      File uploadDir = new File(uploadPath);

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFilename = uuidFile + "." + file.getOriginalFilename();

      file.transferTo(new File(uploadPath + "/practice/" + resultFilename));

      if (studentPractice.getWork() == null) {
        studentPractice.setWork(new Work(resultFilename));
      } else {
        studentPractice.getWork().setProjectUrl(resultFilename);
      }

      studentPractice = studentPracticeRepo.save(studentPractice);
    }

    return new ResponseEntity<>(studentPractice, null, HttpStatus.OK);
  }

  @PutMapping("/teacher")
  public ResponseEntity<StudentPractice> updateScore (
    @AuthenticationPrincipal User user,
    @RequestBody UpdateScoreDto updateScoreDto
    ) {
    User studentUser = userRepo.findById(updateScoreDto.getStudentId()).get();
    Student student = studentRepo.findById(new UserPK(studentUser)).get();
    Practice practice = practiceRepo.findById(updateScoreDto.getPracticeId()).get();
    StudentPractice studentPractice = studentPracticeRepo.findById(new StudentPracticePK(student, practice)).get();

    studentPractice.setScore(updateScoreDto.getScore());
    studentPractice.getWork().setTeacherComment(updateScoreDto.getComment());

    return new ResponseEntity<>(studentPracticeRepo.save(studentPractice), null, HttpStatus.OK);
  }

  @PatchMapping("{id}")
  public ResponseEntity<StudentPractice> updatePractice (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    @RequestBody StudentPractice practice
  ) {
    Student student = studentRepo.findById(new UserPK(user)).get();
    Practice practiceFromDb = practiceRepo.findById(id).get();

    StudentPractice studentPractice = studentPracticeRepo.findById(new StudentPracticePK(student, practiceFromDb)).get();

    if (studentPractice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (practice.getTheme() != null) {
      studentPractice.setTheme(practice.getTheme());
    }

    studentPractice = studentPracticeRepo.save(studentPractice);

    return new ResponseEntity<>(studentPractice, null, HttpStatus.OK);
  }
}
