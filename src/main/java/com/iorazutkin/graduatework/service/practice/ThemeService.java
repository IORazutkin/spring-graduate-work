package com.iorazutkin.graduatework.service.practice;

import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.practice.Theme;
import com.iorazutkin.graduatework.exception.ForbiddenException;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.practice.ThemeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThemeService {
  private final ThemeRepo themeRepo;

  public Theme findById (Long id, User auth) {
    Optional<Theme> themeOptional = themeRepo.findById(id);

    if (!themeOptional.isPresent()) {
      throw new NotFoundException();
    }

    if (checkAuthorities(themeOptional.get(), auth)) {
      return themeOptional.get();
    }

    return null;
  }

  public Boolean checkAuthorities (Theme theme, User auth) {
    if (theme.getPractice().getTeacher().getUser().getUser().equals(auth)) {
      return true;
    }

    throw new ForbiddenException();
  }
}
