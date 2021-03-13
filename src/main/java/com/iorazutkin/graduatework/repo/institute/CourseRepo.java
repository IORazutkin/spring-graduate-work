package com.iorazutkin.graduatework.repo.institute;

import com.iorazutkin.graduatework.entity.institute.Course;
import com.iorazutkin.graduatework.entity.institute.Institute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepo extends JpaRepository<Course, Long> {
  List<Course> findAllByInstituteOrderByTitle(Institute institute);
}
