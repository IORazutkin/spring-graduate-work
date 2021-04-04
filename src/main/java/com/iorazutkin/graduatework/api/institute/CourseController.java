package com.iorazutkin.graduatework.api.institute;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.institute.Course;
import com.iorazutkin.graduatework.entity.institute.Institute;
import com.iorazutkin.graduatework.repo.institute.CourseRepo;
import com.iorazutkin.graduatework.repo.institute.InstituteRepo;
import com.iorazutkin.graduatework.view.institute.CourseView;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {
  private final CourseRepo courseRepo;

  @PostMapping
  @JsonView(CourseView.class)
  public ResponseEntity<List<Course>> findInstituteCourses (@RequestBody Institute institute) {
    return ResponseEntity.ok(courseRepo.findAllByInstituteOrderByTitle(institute));
  }
}
