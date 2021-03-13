package com.iorazutkin.graduatework.repo.institute;

import com.iorazutkin.graduatework.entity.institute.Course;
import com.iorazutkin.graduatework.entity.institute.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupRepo extends JpaRepository<Group, Long> {
  List<Group> findAllByCourseOrderByNum(Course course);
}
