package com.iorazutkin.graduatework.api.institute;

import com.iorazutkin.graduatework.entity.institute.Course;
import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.repo.institute.GroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {
  @Autowired
  private GroupRepo groupRepo;

  @PostMapping
  public ResponseEntity<List<Group>> findAll (@RequestBody Course course) {
    return new ResponseEntity<>(groupRepo.findAllByCourseOrderByNum(course), null, HttpStatus.OK);
  }
}
