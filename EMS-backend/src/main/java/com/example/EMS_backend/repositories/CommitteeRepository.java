package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.Committee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitteeRepository extends JpaRepository<Committee, Long> {
  List<Committee> findByStuActivityId(Long activityId);
  List<Committee> findByDirectorId(Long directorId);
}
