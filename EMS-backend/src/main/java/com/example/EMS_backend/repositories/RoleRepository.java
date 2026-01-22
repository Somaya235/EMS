package com.example.EMS_backend.repositories;



import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.EMS_backend.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String name);
}

