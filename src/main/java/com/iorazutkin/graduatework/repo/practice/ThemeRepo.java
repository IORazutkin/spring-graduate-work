package com.iorazutkin.graduatework.repo.practice;

import com.iorazutkin.graduatework.entity.practice.Practice;
import com.iorazutkin.graduatework.entity.practice.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ThemeRepo extends JpaRepository<Theme, Long> {
  List<Theme> findAllByPractice(Practice practice);
}
