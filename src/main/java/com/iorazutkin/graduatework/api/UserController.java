package com.iorazutkin.graduatework.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.view.user.UserView;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
  @JsonView(UserView.class)
  @GetMapping
  public ResponseEntity<User> getAuthorizationUser(@AuthenticationPrincipal User auth){
    return ResponseEntity.ok(auth);
  }
}
