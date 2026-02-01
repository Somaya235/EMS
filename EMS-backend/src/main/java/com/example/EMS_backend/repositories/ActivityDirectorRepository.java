package com.example.EMS_backend.repositories;

import com.example.EMS_backend.models.ActivityDirector;
import com.example.EMS_backend.models.ActivityDirectorId;
import com.example.EMS_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityDirectorRepository
  extends JpaRepository<ActivityDirector, ActivityDirectorId> {
  
  List<ActivityDirector> findByDirector(User director);
}
