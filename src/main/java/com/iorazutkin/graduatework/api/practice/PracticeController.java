package com.iorazutkin.graduatework.api.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.dto.PracticeDto;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.repo.practice.PracticeRepo;
import com.iorazutkin.graduatework.service.FileService;
import com.iorazutkin.graduatework.service.practice.PracticeService;
import com.iorazutkin.graduatework.service.practice.StudentPracticeService;
import com.iorazutkin.graduatework.view.practice.PracticeView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/practice")
@RequiredArgsConstructor
public class PracticeController {
  private final PracticeRepo practiceRepo;
  private final PracticeService practiceService;
  private final StudentPracticeService studentPracticeService;
  private final FileService fileService;

  @GetMapping
  @JsonView(PracticeView.class)
  public ResponseEntity<List<Practice>> findAllTeacherPractices (
    @AuthenticationPrincipal User auth
  ) {
    return ResponseEntity.ok(practiceService.findAllByUser(auth));
  }

  @GetMapping("{id}")
  @JsonView(PracticeView.class)
  public ResponseEntity<Practice> findById (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(practiceService.findById(id, auth));
  }

  @GetMapping("/dto/{id}")
  @JsonView(PracticeView.class)
  public ResponseEntity<PracticeDto> findDtoById (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(practiceService.findDtoById(id, auth));
  }

  @PostMapping
  @JsonView(PracticeView.class)
  public ResponseEntity<Practice> createPractice (
    @AuthenticationPrincipal User auth,
    @RequestBody PracticeDto practiceDto
  ) {
    Practice practice = practiceService.createByUserAndDto(auth, practiceDto);
    studentPracticeService.createPracticeForAllStudents(practice, practiceDto.getGroupList());

    return ResponseEntity.ok(practice);
  }

  @PutMapping("{id}")
  @JsonView(PracticeView.class)
  public ResponseEntity<Practice> updatePractice (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    @RequestBody PracticeDto practiceDto
  ) {
    Practice practice = practiceService.findById(id, auth);
    studentPracticeService.updatePracticeForAllStudents(practice, practiceDto.getGroupList());
    practiceDto.copyTo(practice);

    return ResponseEntity.ok(practiceRepo.save(practice));
  }

  @PutMapping("{practiceId}/file")
  @JsonView(PracticeView.class)
  public ResponseEntity<Practice> updateFile (
    @AuthenticationPrincipal User auth,
    @RequestParam("file")MultipartFile file,
    @PathVariable Long practiceId
  ) throws IOException {
    Practice practice = practiceService.findById(practiceId, auth);

    String filePath = fileService.loadFile(file, "template");
    fileService.removeFile("template", practice.getTemplate());
    practice.setTemplate(filePath);

    return ResponseEntity.ok(practiceRepo.save(practice));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Boolean> deletePractice (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    Practice practice = practiceService.findById(id, auth);
    practice.setDeleted(true);
    practiceRepo.save(practice);

    return ResponseEntity.ok(true);
  }
}
