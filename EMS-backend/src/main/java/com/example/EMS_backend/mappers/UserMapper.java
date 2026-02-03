package com.example.EMS_backend.mappers;

import com.example.EMS_backend.dto.UserResponseDTO;
import com.example.EMS_backend.models.User;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for User entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    
    /**
     * Convert User entity to UserResponseDTO.
     * Excludes password hash and other sensitive data.
     */
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "major", source = "major")
    @Mapping(target = "role", source = "role")
    UserResponseDTO toResponseDTO(User user);
    
    /**
     * Convert list of User entities to list of UserResponseDTOs.
     */
    List<UserResponseDTO> toResponseDTOList(List<User> users);
}
