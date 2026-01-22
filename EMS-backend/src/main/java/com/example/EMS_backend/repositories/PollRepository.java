package com.example.EMS_backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EMS_backend.models.Poll;
@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
  List<Poll> findByEventId(Long eventId);
}
