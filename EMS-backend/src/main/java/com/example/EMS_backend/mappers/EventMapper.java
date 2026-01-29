package com.example.EMS_backend.mappers;

import com.example.EMS_backend.dto.EventRequestDTO;
import com.example.EMS_backend.dto.EventResponseDTO;
import com.example.EMS_backend.models.Event;
import org.mapstruct.*;

import java.util.List;

/**
 * MapStruct mapper for Event entity and DTOs.
 * Automatically generates implementation at compile time.
 */
@Mapper(componentModel = "spring", uses = {StudentActivityMapper.class, CommitteeMapper.class})
public interface EventMapper {
    
    /**
     * Convert Event entity to EventResponseDTO.
     * Includes nested mappings for activity and committee.
     */
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    EventResponseDTO toResponseDTO(Event event);
    
    /**
     * Convert EventRequestDTO to Event entity.
     * Maps activityId and committeeId to nested objects.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "committee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Event toEntity(EventRequestDTO dto);
    
    /**
     * Convert list of Event entities to list of EventResponseDTOs.
     */
    List<EventResponseDTO> toResponseDTOList(List<Event> events);
    
    /**
     * Update existing Event entity from EventRequestDTO.
     * Ignores null values in the DTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "committee", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromDTO(EventRequestDTO dto, @MappingTarget Event event);
}
