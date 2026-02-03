package com.example.EMS_backend.mappers;

import com.example.EMS_backend.dto.StudentActivityRequestDTO;
import com.example.EMS_backend.dto.StudentActivityResponseDTO;
import com.example.EMS_backend.models.StudentActivity;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for StudentActivity entity and DTOs.
 */
@Mapper(componentModel = "spring")
public interface StudentActivityMapper {
    
    /**
     * Convert StudentActivity entity to StudentActivityResponseDTO.
     */
    @Mapping(target = "presidentId", source = "president.id")
    @Mapping(target = "presidentName", source = "president.fullName")
    StudentActivityResponseDTO toResponseDTO(StudentActivity activity);
    
    /**
     * Convert StudentActivityRequestDTO to StudentActivity entity.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "president", ignore = true)
    @Mapping(target = "webManager", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "vision", source = "vision")
    @Mapping(target = "mission", source = "mission")
    StudentActivity toEntity(StudentActivityRequestDTO dto);
    
    /**
     * Convert list of StudentActivity entities to list of StudentActivityResponseDTOs.
     */
    List<StudentActivityResponseDTO> toResponseDTOList(List<StudentActivity> activities);
    
    /**
     * Update existing StudentActivity entity from StudentActivityRequestDTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "president", ignore = true)
    @Mapping(target = "webManager", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "vision", source = "vision")
    @Mapping(target = "mission", source = "mission")
    void updateEntityFromDTO(StudentActivityRequestDTO dto, @MappingTarget StudentActivity activity);
}
