package com.iorazutkin.graduatework.repo;

import com.iorazutkin.graduatework.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
  User findByUsername(String username);
}
