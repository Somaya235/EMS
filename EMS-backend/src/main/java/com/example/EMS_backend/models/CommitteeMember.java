package com.example.EMS_backend.models;



import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.EMS_backend.models.CommitteeMemberId;

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

  @Column(name = "joined_at")
  private LocalDateTime joinedAt = LocalDateTime.now();

  public CommitteeMember() {}

  public CommitteeMember(Committee committee, User member) {
    this.committee = committee;
    this.member = member;
    this.id = new CommitteeMemberId(committee.getId(), member.getId());
  }

  // getters & setters
}
