package com.iorazutkin.graduatework.repo;

import com.iorazutkin.graduatework.entity.user.Teacher;
import com.iorazutkin.graduatework.entity.user.UserPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, UserPK> {
}
