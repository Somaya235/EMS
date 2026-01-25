package com.example.EMS_backend.models;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="committees")
public class Committee {

  @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

  // Explicit getter for compilation compatibility
  public Long getId() {
    return id;
  }
}
