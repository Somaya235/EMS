package com.example.EMS_backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.EMS_backend.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);
  boolean existsByEmail(String email);
  Optional<User> findByIdAndEnabled(Long id, Boolean enabled);
  
  // Search students by name (case-insensitive partial match)
  @Query("SELECT u FROM User u WHERE " +
         "LOWER(u.fullName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND " +
         "u.enabled = true")
  List<User> searchByName(@Param("searchTerm") String searchTerm);
  
  // Find enabled user by ID
  Optional<User> findByIdAndEnabledTrue(Long id);
  
  // Find all enabled users
  @Query("SELECT u FROM User u WHERE u.enabled = true")
  List<User> findByEnabledTrue();
  
  // Search students by college ID (exact match)
  Optional<User> findByCollageIdAndEnabledTrue(String collageId);
}
