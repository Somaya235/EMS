package com.example.EMS_backend.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="votes",
  uniqueConstraints=@UniqueConstraint(columnNames={"option_id","user_id"}))
public class Vote {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne @JoinColumn(name="option_id")
  private PollOption option;

  @ManyToOne @JoinColumn(name="user_id")
  private User user;

  private LocalDateTime votedAt = LocalDateTime.now();
}
