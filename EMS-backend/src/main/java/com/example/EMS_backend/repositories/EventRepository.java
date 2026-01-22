package com.example.EMS_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EMS_backend.models.Event;
import com.example.EMS_backend.models.StudentActivity;
@Repository

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByActivity(StudentActivity activity);
  List<Event> findByCommitteeId(Long committeeId);
}
