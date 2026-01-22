package com.example.EMS_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.EMS_backend.models.StudentActivity;

public interface StudentActivityRepository extends JpaRepository<StudentActivity, Long> {
}
