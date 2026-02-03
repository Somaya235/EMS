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

  @Column(nullable = false)
  private Boolean enabled = true;

  @Column(columnDefinition = "TEXT")
  private String vision;

  @Column(columnDefinition = "TEXT")
  private String mission;

  // One activity → one president
  @OneToOne
  @JoinColumn(name="president_id", unique=true)
  private User president;

  // One activity → one web manager (optional)
  @ManyToOne
  @JoinColumn(name="web_manager_id")
  private User webManager;

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

  public User getWebManager() {
    return webManager;
  }

  public void setWebManager(User webManager) {
    this.webManager = webManager;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getVision() {
    return vision;
  }

  public void setVision(String vision) {
    this.vision = vision;
  }

  public String getMission() {
    return mission;
  }

  public void setMission(String mission) {
    this.mission = mission;
  }
}
