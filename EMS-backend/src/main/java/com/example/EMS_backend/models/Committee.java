package com.example.EMS_backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Committee extends AuditableEntity {

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

  // Manual getters and setters to avoid Lombok compilation issues
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public StudentActivity getActivity() {
    return activity;
  }

  public void setActivity(StudentActivity activity) {
    this.activity = activity;
  }

  public User getDirector() {
    return director;
  }

  public void setDirector(User director) {
    this.director = director;
  }
}
