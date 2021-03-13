package com.iorazutkin.graduatework.repo;

import com.iorazutkin.graduatework.entity.Teacher;
import com.iorazutkin.graduatework.entity.UserPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepo extends JpaRepository<Teacher, UserPK> {
}
