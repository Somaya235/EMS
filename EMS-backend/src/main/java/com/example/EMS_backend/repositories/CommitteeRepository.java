package com.example.EMS_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EMS_backend.models.Committee;
import com.example.EMS_backend.models.StudentActivity;

@Repository
public interface CommitteeRepository extends JpaRepository<Committee, Long> {
  List<Committee> findByActivity(StudentActivity activity);
  List<Committee> findByDirectorId(Long directorId);
}
