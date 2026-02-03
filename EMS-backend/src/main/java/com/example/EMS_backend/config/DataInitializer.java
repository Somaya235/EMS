package com.example.EMS_backend.config;

import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setFullName("System Admin");
            admin.setEmail("admin@ems.com");
            admin.setPasswordHash(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
            System.out.println("DataInitializer: Created default admin user (admin@ems.com / admin123)");
        }
        System.out.println("DataInitializer: Using hybrid authorization (Role-based + Relationship-based)");
    }
}
