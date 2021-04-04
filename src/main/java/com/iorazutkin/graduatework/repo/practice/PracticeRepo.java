package com.iorazutkin.graduatework.repo.practice;

import com.iorazutkin.graduatework.entity.user.Teacher;
import com.iorazutkin.graduatework.entity.practice.Practice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeRepo extends JpaRepository<Practice, Long> {
  List<Practice> findAllByTeacherAndDeletedIsFalse(Teacher teacher);
}
