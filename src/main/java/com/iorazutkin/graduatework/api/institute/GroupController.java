package com.iorazutkin.graduatework.api.institute;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.institute.Course;
import com.iorazutkin.graduatework.entity.institute.Group;
import com.iorazutkin.graduatework.repo.institute.GroupRepo;
import com.iorazutkin.graduatework.view.institute.CourseView;
import com.iorazutkin.graduatework.view.institute.GroupView;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GroupController {
  private final GroupRepo groupRepo;

  @PostMapping
  @JsonView(GroupView.class)
  public ResponseEntity<List<Group>> findAll (@RequestBody Course course) {
    return ResponseEntity.ok(groupRepo.findAllByCourseOrderByNum(course));
  }
}
