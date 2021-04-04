package com.iorazutkin.graduatework.repo;

import com.iorazutkin.graduatework.entity.user.Student;
import com.iorazutkin.graduatework.entity.user.UserPK;
import com.iorazutkin.graduatework.entity.institute.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, UserPK> {
  List<Student> findAllByGroup(Group group);
}
