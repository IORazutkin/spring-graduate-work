package com.iorazutkin.graduatework.api.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.dto.UpdateScoreDto;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.StudentPractice;
import com.iorazutkin.graduatework.entity.practice.Work;
import com.iorazutkin.graduatework.repo.practice.StudentPracticeRepo;
import com.iorazutkin.graduatework.service.FileService;
import com.iorazutkin.graduatework.service.practice.StudentPracticeService;
import com.iorazutkin.graduatework.view.practice.StudentPracticeAuthListView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeAuthView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeListView;
import com.iorazutkin.graduatework.view.practice.StudentPracticeView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/student-practice")
@RequiredArgsConstructor
public class StudentPracticeController {
  private final StudentPracticeRepo studentPracticeRepo;
  private final StudentPracticeService studentPracticeService;
  private final FileService fileService;

  @GetMapping
  @JsonView(StudentPracticeAuthListView.class)
  public ResponseEntity<List<StudentPractice>> findAll (
    @AuthenticationPrincipal User auth
  ) {
    return ResponseEntity.ok(studentPracticeService.findAll(auth));
  }

  @GetMapping("{id}")
  @JsonView(StudentPracticeAuthView.class)
  public ResponseEntity<StudentPractice> getOne (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(studentPracticeService.findByUserAndPracticeId(auth, id));
  }

  @GetMapping("/teacher")
  @JsonView(StudentPracticeView.class)
  public ResponseEntity<StudentPractice> findByPracticeAndStudent (
    @RequestParam(name = "practice_id") Long practiceId,
    @RequestParam(name = "student_id") Long studentId
  ) {
    return ResponseEntity.ok(studentPracticeService.findByUserIdAndPracticeId(studentId, practiceId));
  }

  @PostMapping
  @JsonView(StudentPracticeListView.class)
  public ResponseEntity<Map<String, List<StudentPractice>>> findByPractice (
    @RequestBody Practice practice
  ) {
    return ResponseEntity.ok(studentPracticeService.findMapByPractice(practice));
  }

  @PutMapping("{practiceId}")
  @JsonView(StudentPracticeAuthView.class)
  public ResponseEntity<StudentPractice> updateWorkFile (
    @AuthenticationPrincipal User auth,
    @PathVariable Long practiceId,
    @RequestParam("file") MultipartFile file
  ) throws IOException {
    StudentPractice studentPractice = studentPracticeService.findByUserAndPracticeId(auth, practiceId);

    String filePath = fileService.loadFile(file, "practice");
    if (studentPractice.getWork() == null) {
      studentPractice.setWork(new Work(filePath));
    } else {
      fileService.removeFile("practice", studentPractice.getWork().getProjectUrl());
      studentPractice.getWork().setProjectUrl(filePath);
    }

    return ResponseEntity.ok(studentPracticeRepo.save(studentPractice));
  }

  @PutMapping("/teacher")
  @JsonView(StudentPracticeView.class)
  public ResponseEntity<StudentPractice> updateScore (
    @RequestBody UpdateScoreDto updateScoreDto
  ) {
    StudentPractice studentPractice = studentPracticeService.findByUserIdAndPracticeId(
      updateScoreDto.getStudentId(),
      updateScoreDto.getPracticeId()
    );

    studentPractice.setScore(updateScoreDto.getScore());
    studentPractice.getWork().setTeacherComment(updateScoreDto.getComment());

    return ResponseEntity.ok(studentPracticeRepo.save(studentPractice));
  }

  @PatchMapping("{id}")
  @JsonView(StudentPracticeAuthView.class)
  public ResponseEntity<StudentPractice> updatePractice (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    @RequestBody StudentPractice practice
  ) {
    StudentPractice studentPractice = studentPracticeService.findByUserAndPracticeId(auth, id);

    if (practice.getTheme() != null) {
      studentPractice.setTheme(practice.getTheme());
    }

    return ResponseEntity.ok(studentPracticeRepo.save(studentPractice));
  }
}
