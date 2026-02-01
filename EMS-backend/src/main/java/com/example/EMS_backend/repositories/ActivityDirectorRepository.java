package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.ActivityDirector;
import com.example.EMS_backend.models.ActivityDirectorId;
import com.example.EMS_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityDirectorRepository
  extends JpaRepository<ActivityDirector, ActivityDirectorId> {
  
  List<ActivityDirector> findByDirector(User director);
}
