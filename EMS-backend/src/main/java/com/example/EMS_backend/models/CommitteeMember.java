package com.example.EMS_backend.models;



import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter

@Entity
@Table(name = "committee_members")
public class CommitteeMember {

  @EmbeddedId
  private CommitteeMemberId id;

  @ManyToOne
  @MapsId("committeeId")
  @JoinColumn(name = "committee_id")
  private Committee committee;

  @ManyToOne
  @MapsId("committeeMemberId")
  @JoinColumn(name = "committee_member")
  private User member;

  @ManyToOne
  @JoinColumn(name = "head_member_id")
  private User headMember;

  @Column(name = "joined_at")
  private LocalDateTime joinedAt = LocalDateTime.now();

  public CommitteeMember() {}

  public CommitteeMember(Committee committee, User member) {
    this.committee = committee;
    this.member = member;
    this.id = new CommitteeMemberId(committee.getId(), member.getId());
  }

  // Getters and Setters
  public CommitteeMemberId getId() {
    return id;
  }

  public void setId(CommitteeMemberId id) {
    this.id = id;
  }

  public Committee getCommittee() {
    return committee;
  }

  public void setCommittee(Committee committee) {
    this.committee = committee;
  }

  public User getMember() {
    return member;
  }

  public void setMember(User member) {
    this.member = member;
  }

  public User getHeadMember() {
    return headMember;
  }

  public void setHeadMember(User headMember) {
    this.headMember = headMember;
  }

  public LocalDateTime getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(LocalDateTime joinedAt) {
    this.joinedAt = joinedAt;
  }
}
