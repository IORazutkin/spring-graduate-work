package com.iorazutkin.graduatework.repo.practice;

import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.StudentPractice;
import com.iorazutkin.graduatework.entity.practice.StudentPracticePK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentPracticeRepo extends JpaRepository<StudentPractice, StudentPracticePK> {
  List<StudentPractice> findAllById_Practice (Practice practice);
  List<StudentPractice> findAllById_StudentAndId_Practice_DeletedIsFalse (Student student);
}
