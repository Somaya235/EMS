package com.example.EMS_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.EMS_backend.models.StudentActivity;
import com.example.EMS_backend.models.User;

public interface StudentActivityRepository extends JpaRepository<StudentActivity, Long> {

    boolean existsByName(String name);

    boolean existsByPresident(User president);
}
