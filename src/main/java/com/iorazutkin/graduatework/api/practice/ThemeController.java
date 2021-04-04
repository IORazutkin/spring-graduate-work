package com.iorazutkin.graduatework.api.practice;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.dto.ThemeDto;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.Theme;
import com.iorazutkin.graduatework.repo.practice.ThemeRepo;
import com.iorazutkin.graduatework.service.practice.PracticeService;
import com.iorazutkin.graduatework.service.practice.ThemeService;
import com.iorazutkin.graduatework.view.practice.ThemeView;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/theme")
@RequiredArgsConstructor
public class ThemeController {
  private final ThemeRepo themeRepo;
  private final ThemeService themeService;
  private final PracticeService practiceService;

  @GetMapping("{id}")
  @JsonView(ThemeView.class)
  public ResponseEntity<List<Theme>> findAllByPractice (
    @PathVariable Long id
  ) {
    return ResponseEntity.ok(themeRepo.findAllByPracticeAndDeletedIsFalse(practiceService.findById(id)));
  }

  @PostMapping
  @JsonView(ThemeView.class)
  public ResponseEntity<Theme> addTheme (
    @AuthenticationPrincipal User auth,
    @RequestBody ThemeDto data
  ) {
    Practice practice = practiceService.findById(data.getPracticeId(), auth);

    Theme theme = new Theme();
    theme.setPractice(practice);
    theme.setTitle(data.getTitle());

    return new ResponseEntity<>(themeRepo.save(theme), HttpStatus.CREATED);
  }

  @PatchMapping("{id}")
  @JsonView(ThemeView.class)
  public ResponseEntity<Theme> updateTheme (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id,
    @RequestBody Theme theme
  ) {
    Theme themeFromDb = themeService.findById(id, auth);
    themeFromDb.setTitle(theme.getTitle());

    return ResponseEntity.ok(themeRepo.save(themeFromDb));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Boolean> deleteTheme (
    @AuthenticationPrincipal User auth,
    @PathVariable Long id
  ) {
    Theme theme = themeService.findById(id, auth);
    theme.setDeleted(true);
    themeRepo.save(theme);

    return ResponseEntity.ok(true);
  }
}
