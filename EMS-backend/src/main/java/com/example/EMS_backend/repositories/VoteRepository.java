package com.example.EMS_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EMS_backend.models.Vote;

import java.util.Optional;

@Repository

public interface VoteRepository extends JpaRepository<Vote, Long> {
  Optional<Vote> findByOptionIdAndUserId(Long optionId, Long userId);
  long countByOptionId(Long optionId);
}
