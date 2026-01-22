package com.example.EMS_backend.models;


import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CommitteeMemberId implements Serializable {

  @Column(name = "committee_id")
  private Long committeeId;

  @Column(name = "committee_member")
  private Long committeeMemberId;

  public CommitteeMemberId() {}

  public CommitteeMemberId(Long committeeId, Long committeeMemberId) {
    this.committeeId = committeeId;
    this.committeeMemberId = committeeMemberId;
  }

  // equals & hashCode (required for composite key)
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CommitteeMemberId)) return false;
    CommitteeMemberId that = (CommitteeMemberId) o;
    return Objects.equals(committeeId, that.committeeId) &&
      Objects.equals(committeeMemberId, that.committeeMemberId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(committeeId, committeeMemberId);
  }

  // Getters and Setters
  public Long getCommitteeId() {
    return committeeId;
  }

  public void setCommitteeId(Long committeeId) {
    this.committeeId = committeeId;
  }

  public Long getCommitteeMemberId() {
    return committeeMemberId;
  }

  public void setCommitteeMemberId(Long committeeMemberId) {
    this.committeeMemberId = committeeMemberId;
  }
}

