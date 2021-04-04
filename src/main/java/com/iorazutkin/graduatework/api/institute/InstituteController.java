package com.iorazutkin.graduatework.api.institute;

import com.iorazutkin.graduatework.entity.institute.Institute;
import com.iorazutkin.graduatework.repo.institute.InstituteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/institute")
@RequiredArgsConstructor
public class InstituteController {
  private final InstituteRepo instituteRepo;

  @GetMapping
  public ResponseEntity<List<Institute>> findAll () {
    return ResponseEntity.ok(instituteRepo.findAll(Sort.by("title")));
  }
}
