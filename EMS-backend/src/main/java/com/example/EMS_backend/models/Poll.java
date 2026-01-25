package com.example.EMS_backend.models;

<<<<<<< HEAD
=======
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
>>>>>>> 98d1192c07e7409e2a397fb1b819efe078655bb7
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
<<<<<<< HEAD
=======
import java.util.Set;
>>>>>>> 98d1192c07e7409e2a397fb1b819efe078655bb7

@Entity
@Table(name="polls")
public class Poll {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private Boolean isPublic;

  @ElementCollection
  @CollectionTable(name="poll_tags", joinColumns=@JoinColumn(name="poll_id"))
  @Column(name="tag")
  private Set<String> tags;

>>>>>>> 98d1192c07e7409e2a397fb1b819efe078655bb7
=======
  @ManyToOne
  @JoinColumn(name="event_id")
  private Event event;

  @ElementCollection
  @CollectionTable(name="poll_tags", joinColumns=@JoinColumn(name="poll_id"))
  @Column(name="tag")
  private Set<String> tags;
=======
  @ElementCollection
  @CollectionTable(name="poll_tags", joinColumns=@JoinColumn(name="poll_id"))
  @Column(name="tag")
  private Set<String> tags;

>>>>>>> 98d1192c07e7409e2a397fb1b819efe078655bb7
  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Boolean getIsPublic() {
    return isPublic;
  }

  public void setIsPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }
}
