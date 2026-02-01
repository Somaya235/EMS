package com.example.EMS_backend.mappers;

import com.example.EMS_backend.dto.ActivityDirectorRequestDTO;
import com.example.EMS_backend.dto.ActivityDirectorResponseDTO;
import com.example.EMS_backend.models.ActivityDirector;
import com.example.EMS_backend.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ActivityDirectorMapper {

    @Mapping(target = "activityId", source = "activityDirector.id.activityId")
    @Mapping(target = "directorId", source = "activityDirector.id.directorId")
    @Mapping(target = "directorFullName", source = "activityDirector.director.fullName")
    @Mapping(target = "directorEmail", source = "activityDirector.director.email")
    @Mapping(target = "positionName", source = "activityDirector.name") // map name to positionName
    @Mapping(target = "jobDescription", source = "activityDirector.jobDesc") // map jobDesc to jobDescription
    ActivityDirectorResponseDTO toResponseDTO(ActivityDirector activityDirector);

    List<ActivityDirectorResponseDTO> toResponseDTOList(List<ActivityDirector> activityDirectors);

    // Method to create an ActivityDirector entity from a DTO and a User (for director)
    default ActivityDirector toEntity(ActivityDirectorRequestDTO dto, User director) {
        if (dto == null || director == null) {
            return null;
        }
        ActivityDirector activityDirector = new ActivityDirector();
        activityDirector.setDirector(director);
        activityDirector.setName(dto.getPositionName());
        activityDirector.setJobDesc(dto.getJobDescription());
        return activityDirector;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activity", ignore = true)
    @Mapping(target = "director", ignore = true)
    @Mapping(target = "assignedAt", ignore = true)
    @Mapping(target = "name", source = "dto.positionName") // map positionName to name
    @Mapping(target = "jobDesc", source = "dto.jobDescription") // map jobDescription to jobDesc
    void updateEntityFromDTO(ActivityDirectorRequestDTO dto, @MappingTarget ActivityDirector activityDirector);
}
