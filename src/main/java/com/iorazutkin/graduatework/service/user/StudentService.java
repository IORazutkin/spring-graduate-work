package com.iorazutkin.graduatework.service.user;

import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.user.UserPK;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.StudentRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
  private final StudentRepo studentRepo;

  public Student findByUser (User user) {
    Optional<Student> studentOptional = studentRepo.findById(new UserPK(user));

    if (studentOptional.isPresent()) {
      return studentOptional.get();
    }

    throw new NotFoundException();
  }
}
