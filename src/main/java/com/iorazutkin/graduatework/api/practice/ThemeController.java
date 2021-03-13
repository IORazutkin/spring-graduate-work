package com.iorazutkin.graduatework.api.practice;

import com.iorazutkin.graduatework.dto.ThemeDto;
import com.iorazutkin.graduatework.entity.User;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.Theme;
import com.iorazutkin.graduatework.repo.practice.PracticeRepo;
import com.iorazutkin.graduatework.repo.practice.ThemeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theme")
public class ThemeController {
  @Autowired
  private ThemeRepo themeRepo;

  @Autowired
  private PracticeRepo practiceRepo;

  @GetMapping("{id}")
  public ResponseEntity<List<Theme>> findAllByPractice (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Practice practice = practiceRepo.findById(id).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    List<Theme> themes = themeRepo.findAllByPractice(practice);

    return new ResponseEntity<>(themes, null, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Theme> addTheme (
    @AuthenticationPrincipal User user,
    @RequestBody ThemeDto data
  ) {
    Practice practice = practiceRepo.findById(data.getPracticeId()).get();

    if (practice == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!practice.getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    Theme theme = new Theme();
    theme.setPractice(practice);
    theme.setTitle(data.getTitle());

    theme = themeRepo.save(theme);

    return new ResponseEntity<>(theme, null, HttpStatus.CREATED);
  }

  @PatchMapping("{id}")
  public ResponseEntity<Theme> updateTheme (
    @AuthenticationPrincipal User user,
    @PathVariable Long id,
    @RequestBody Theme theme
  ) {
    Theme themeFromDb = themeRepo.findById(id).get();

    if (themeFromDb == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NOT_FOUND);
    }

    if (!themeFromDb.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    themeFromDb.setTitle(theme.getTitle());
    theme = themeRepo.save(themeFromDb);

    return new ResponseEntity<>(theme, null, HttpStatus.OK);
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Object> deleteTheme (
    @AuthenticationPrincipal User user,
    @PathVariable Long id
  ) {
    Theme theme = themeRepo.findById(id).get();

    if (theme == null) {
      return new ResponseEntity<>(null, null, HttpStatus.NO_CONTENT);
    }

    if (!theme.getPractice().getTeacher().getUser().getUser().equals(user)) {
      return new ResponseEntity<>(null, null, HttpStatus.FORBIDDEN);
    }

    themeRepo.delete(theme);

    return new ResponseEntity<>(null, null, HttpStatus.OK);
  }
}
