package com.iorazutkin.graduatework.service.user;

import com.iorazutkin.graduatework.entity.user.Teacher;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.user.UserPK;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.TeacherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
  private final TeacherRepo teacherRepo;

  public Teacher findByUser (User user) {
    Optional<Teacher> teacherOptional = teacherRepo.findById(new UserPK(user));

    if (teacherOptional.isPresent()) {
      return teacherOptional.get();
    }

    throw new NotFoundException();
  }
}
