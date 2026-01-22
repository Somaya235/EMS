package com.example.EMS_backend.models;
import jakarta.persistence.*;


@Entity
@Table(name="student_activities")
public class StudentActivity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String category;

  // One activity → one president
  @OneToOne
  @JoinColumn(name="president_id", unique=true)
  private User president;
}
