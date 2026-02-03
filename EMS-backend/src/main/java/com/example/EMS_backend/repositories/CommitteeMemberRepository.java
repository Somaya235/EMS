package com.example.EMS_backend.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.EMS_backend.models.CommitteeMember;
import com.example.EMS_backend.models.CommitteeMemberId;
@Repository
public interface CommitteeMemberRepository
  extends JpaRepository<CommitteeMember, CommitteeMemberId> {

  List<CommitteeMember> findByCommitteeId(Long committeeId);
  List<CommitteeMember> findByMemberId(Long memberId);
  int countByCommitteeId(Long committeeId);
}
