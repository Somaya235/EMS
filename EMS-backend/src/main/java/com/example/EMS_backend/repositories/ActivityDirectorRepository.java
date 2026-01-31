package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.ActivityDirector;
import com.example.EMS_backend.models.ActivityDirectorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityDirectorRepository
  extends JpaRepository<ActivityDirector, ActivityDirectorId> {
}
