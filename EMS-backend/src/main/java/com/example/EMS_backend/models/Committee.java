package com.example.EMS_backend.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="committees")
public class Committee {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;

  @ManyToOne
  @JoinColumn(name="stu_activity_id")
  private StudentActivity activity;

  // Director (many committees → one director)
  @ManyToOne
  @JoinColumn(name="director_id")
  private User director;

  // Head (1:1)
  @OneToOne
  @JoinColumn(name="head_id", unique=true)
  private User head;

  // Members (M:N)
  @ManyToMany
  @JoinTable(
    name="committee_members",
    joinColumns=@JoinColumn(name="committee_id"),
    inverseJoinColumns=@JoinColumn(name="committee_member")
  )
  private Set<User> members = new HashSet<>();
}
