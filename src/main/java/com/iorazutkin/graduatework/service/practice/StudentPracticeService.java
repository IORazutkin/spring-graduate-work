package com.iorazutkin.graduatework.service.practice;

import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.StudentPractice;
import com.iorazutkin.graduatework.entity.practice.StudentPracticePK;
import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.StudentRepo;
import com.iorazutkin.graduatework.repo.practice.StudentPracticeRepo;
import com.iorazutkin.graduatework.service.user.StudentService;
import com.iorazutkin.graduatework.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentPracticeService {
  private final StudentPracticeRepo studentPracticeRepo;
  private final StudentRepo studentRepo;
  private final StudentService studentService;
  private final PracticeService practiceService;
  private final UserService userService;

  public List<StudentPractice> findAll (User user) {
    Student student = studentService.findByUser(user);
    return studentPracticeRepo.findAllById_StudentAndId_Practice_DeletedIsFalse(student);
  }

  public StudentPractice findByUserAndPracticeId (User user, Long id) {
    Student student = studentService.findByUser(user);
    Practice practice = practiceService.findById(id);

    Optional<StudentPractice> studentPracticeOptional = studentPracticeRepo.findById(
      new StudentPracticePK(student, practice)
    );

    if (studentPracticeOptional.isPresent()) {
      return studentPracticeOptional.get();
    }

    throw new NotFoundException();
  }

  public StudentPractice findByUserIdAndPracticeId (Long userId, Long practiceId) {
    User user = userService.findById(userId);
    return findByUserAndPracticeId(user, practiceId);
  }

  public Map<String, List<StudentPractice>> findMapByPractice (Practice practice) {
    List<StudentPractice> studentPractices = studentPracticeRepo.findAllById_Practice(practice);
    Map<String, List<StudentPractice>> map = new HashMap<>();

    studentPractices.forEach((studentPractice) -> {
      Group group = studentPractice.getId().getStudent().getGroup();

      if (map.containsKey(group.getNum())) {
        map.get(group.getNum()).add(studentPractice);
      } else {
        map.put(group.getNum(), Collections.singletonList(studentPractice));
      }
    });

    return map;
  }

  public void createPracticeForAllStudents (Practice practice, List<Group> groupList) {
    for (Group group: groupList) {
      List<Student> students = studentRepo.findAllByGroup(group);

      for (Student student: students) {
        StudentPractice studentPractice = new StudentPractice(student, practice);
        studentPracticeRepo.save(studentPractice);
      }
    }
  }

  public void updatePracticeForAllStudents (Practice practice, List<Group> groupList) {
    List<StudentPractice> studentPractices = studentPracticeRepo.findAllById_Practice(practice);

    studentPracticeRepo.deleteAll(
      studentPractices.stream()
        .filter(item -> !groupList.contains(item.getId().getStudent().getGroup()))
        .collect(Collectors.toList())
    );

    for (Group group: groupList) {
      if (studentPractices.stream().anyMatch(item -> item.getId().getStudent().getGroup().equals(group))) {
        continue;
      }

      List<Student> students = studentRepo.findAllByGroup(group);

      for (Student student: students) {
        StudentPractice studentPractice = new StudentPractice(student, practice);
        studentPracticeRepo.save(studentPractice);
      }
    }
  }
}
