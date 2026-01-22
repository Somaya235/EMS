package com.example.EMS_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EMS_backend.models.Event;
@Repository

public interface EventRepository extends JpaRepository<Event, Long> {
  List<Event> findByStuActivityId(Long activityId);
  List<Event> findByCommitteeId(Long committeeId);
}
