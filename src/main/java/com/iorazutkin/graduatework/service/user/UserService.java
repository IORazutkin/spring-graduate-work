package com.iorazutkin.graduatework.service.user;

import com.iorazutkin.graduatework.entity.user.User;
import com.iorazutkin.graduatework.exception.NotFoundException;
import com.iorazutkin.graduatework.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
  private final UserRepo userRepo;

  public User findById (Long id) {
    Optional<User> userOptional = userRepo.findById(id);

    if (userOptional.isPresent()) {
      return userOptional.get();
    }

    throw new NotFoundException();
  }

  @Override
  public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
    return userRepo.findByUsername(username);
  }
}
