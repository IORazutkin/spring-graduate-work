package com.iorazutkin.graduatework.service.practice;

import com.iorazutkin.graduatework.dto.PracticeDto;
import com.iorazutkin.graduatework.entity.user.Teacher;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.StudentPractice;
import com.iorazutkin.graduatework.exception.ForbiddenException;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.practice.PracticeRepo;
import com.iorazutkin.graduatework.repo.practice.StudentPracticeRepo;
import com.iorazutkin.graduatework.service.user.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PracticeService {
  private final PracticeRepo practiceRepo;
  private final TeacherService teacherService;
  private final StudentPracticeRepo studentPracticeRepo;

  public Practice findById (Long id) {
    Optional<Practice> practiceOptional = practiceRepo.findById(id);

    return practiceOptional.orElseGet(
      () -> {
        throw new NotFoundException();
      }
    );
  }

  public Practice findById (Long id, User auth) {
    Practice practice = findById(id);

    if (practice.getTeacher().getUser().getUser().equals(auth)) {
      return practice;
    }

    throw new ForbiddenException();
  }

  public List<Practice> findAllByUser (User user) {
    Teacher teacher = teacherService.findByUser(user);
    return practiceRepo.findAllByTeacherAndDeletedIsFalse(teacher);
  }

  public PracticeDto findDtoById (Long id, User user) {
    Practice practice = findById(id, user);
    List<StudentPractice> studentPractices = studentPracticeRepo.findAllById_Practice(practice);

    List<Group> groups = studentPractices.stream()
      .map((item) -> item.getId().getStudent().getGroup())
      .distinct().collect(Collectors.toList());

    return new PracticeDto(practice, groups);
  }

  public Practice createByUserAndDto (User user, PracticeDto practiceDto) {
    Teacher teacher = teacherService.findByUser(user);
    Practice practice = practiceDto.toPractice();
    practice.setTeacher(teacher);

    return practiceRepo.save(practice);
  }
}
