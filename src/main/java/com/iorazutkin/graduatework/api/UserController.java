package com.iorazutkin.graduatework.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
  @JsonView({ User.Info.class })
  @GetMapping
  public ResponseEntity<User> getAuthorizationUser(@AuthenticationPrincipal User user){
    return new ResponseEntity<>(user, null, HttpStatus.OK);
  }
}
