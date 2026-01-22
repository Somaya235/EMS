package com.example.EMS_backend.config;

import com.example.EMS_backend.models.Role;
import com.example.EMS_backend.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        if (roleRepository.count() == 0) {
            Role superAdminRole = new Role();
            superAdminRole.setName("super_admin");
            roleRepository.save(superAdminRole);

            Role activityPresidentRole = new Role();
            activityPresidentRole.setName("activity_president");
            roleRepository.save(activityPresidentRole);

            Role webManagerRole = new Role();
            webManagerRole.setName("web_manager");
            roleRepository.save(webManagerRole);

            Role activityDirectorRole = new Role();
            activityDirectorRole.setName("activity_director");
            roleRepository.save(activityDirectorRole);

            Role committeeHeadRole = new Role();
            committeeHeadRole.setName("committee_head");
            roleRepository.save(committeeHeadRole);

            Role committeeMemberRole = new Role();
            committeeMemberRole.setName("committee_member");
            roleRepository.save(committeeMemberRole);

            Role memberRole = new Role();
            memberRole.setName("member");
            roleRepository.save(memberRole);

            System.out.println("Initialized roles in database");
        }
    }
}
