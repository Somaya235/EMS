package com.example.EMS_backend.mappers;

import com.example.EMS_backend.dto.CommitteeRequestDTO;
import com.example.EMS_backend.dto.CommitteeResponseDTO;
import com.example.EMS_backend.models.Committee;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Committee entity and DTOs.
 */
@Mapper(
        componentModel = "spring",
        uses = {StudentActivityMapper.class, UserMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommitteeMapper {
    
    /**
     * Convert Committee entity to CommitteeResponseDTO.
     * MapStruct will automatically map matching fields, including
     * nested StudentActivity -> StudentActivityResponseDTO via StudentActivityMapper.
     */
    CommitteeResponseDTO toResponseDTO(Committee committee);
    
    /**
     * Convert CommitteeRequestDTO to Committee entity.
     * Activity, director, head, members are set in service layer.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Committee toEntity(CommitteeRequestDTO dto);
    
    /**
     * Convert list of Committee entities to list of CommitteeResponseDTOs.
     */
    List<CommitteeResponseDTO> toResponseDTOList(List<Committee> committees);
    
    /**
     * Update existing Committee entity from CommitteeRequestDTO.
     * Activity, director, head, members are managed separately.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntityFromDTO(CommitteeRequestDTO dto, @MappingTarget Committee committee);
}
