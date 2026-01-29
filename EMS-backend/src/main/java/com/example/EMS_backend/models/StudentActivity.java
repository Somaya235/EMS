package com.example.EMS_backend.models;
import jakarta.persistence.*;


@Entity
@Table(name="student_activities")
public class StudentActivity extends AuditableEntity {

  @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  private String description;
  @Column(nullable = false)
  private String category;

  // One activity → one president
  @OneToOne
  @JoinColumn(name="president_id", unique=true)
  private User president;

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public User getPresident() {
    return president;
  }

  public void setPresident(User president) {
    this.president = president;
  }
}
