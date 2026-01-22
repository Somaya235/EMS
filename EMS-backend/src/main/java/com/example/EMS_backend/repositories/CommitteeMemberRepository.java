package com.example.EMS_backend.repositories;
import com.example.EMS_backend.models.CommitteeMember;
import com.example.EMS_backend.models.CommitteeMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommitteeMemberRepository
  extends JpaRepository<CommitteeMember, CommitteeMemberId> {

  List<CommitteeMember> findByCommitteeId(Long committeeId);
  List<CommitteeMember> findByCommitteeMemberId(Long userId);
}

