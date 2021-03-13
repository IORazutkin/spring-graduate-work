package com.iorazutkin.graduatework.api.institute;

import com.iorazutkin.graduatework.entity.institute.Course;
import com.iorazutkin.graduatework.entity.institute.Institute;
import com.iorazutkin.graduatework.repo.institute.CourseRepo;
import com.iorazutkin.graduatework.repo.institute.InstituteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course")
public class CourseController {
  @Autowired
  private CourseRepo courseRepo;

  @Autowired
  private InstituteRepo instituteRepo;

  @PostMapping
  public ResponseEntity<List<Course>> findInstituteCourses (@RequestBody Institute institute) {
    return new ResponseEntity<>(courseRepo.findAllByInstituteOrderByTitle(institute), null, HttpStatus.OK);
  }
}
