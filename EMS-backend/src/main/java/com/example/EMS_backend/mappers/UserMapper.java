package com.example.EMS_backend.mappers;

import com.example.EMS_backend.dto.UserResponseDTO;
import com.example.EMS_backend.models.Role;
import com.example.EMS_backend.models.User;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for User entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    /**
     * Convert User entity to UserResponseDTO.
     * Excludes password hash and other sensitive data.
     */
    @Mapping(target = "roles", expression = "java(mapRolesToStrings(user.getRoles()))")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    // MapStruct will map fields with matching names automatically (e.g., email, grade, cvAttachment, profileImage).
    // We explicitly map "major" from the underlying "grade" field to satisfy the student profile requirements.
    @Mapping(target = "major", source = "grade")
    UserResponseDTO toResponseDTO(User user);
    
    /**
     * Convert list of User entities to list of UserResponseDTOs.
     */
    List<UserResponseDTO> toResponseDTOList(List<User> users);
    
    /**
     * Helper method to map Role entities to role name strings.
     */
    default Set<String> mapRolesToStrings(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }
}
