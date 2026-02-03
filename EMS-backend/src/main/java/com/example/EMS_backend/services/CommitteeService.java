package com.example.EMS_backend.services;

import com.example.EMS_backend.dto.AddMemberRequestDTO;
import com.example.EMS_backend.dto.CommitteeRequestDTO;
import com.example.EMS_backend.dto.CommitteeResponseDTO;
import com.example.EMS_backend.dto.UserResponseDTO;
import com.example.EMS_backend.exceptions.ResourceNotFoundException;
import com.example.EMS_backend.exceptions.ForbiddenException;
import com.example.EMS_backend.models.Committee;
import com.example.EMS_backend.models.StudentActivity;
import com.example.EMS_backend.models.User;
import com.example.EMS_backend.repositories.CommitteeRepository;
import com.example.EMS_backend.repositories.StudentActivityRepository;
import com.example.EMS_backend.repositories.UserRepository;
import com.example.EMS_backend.mappers.CommitteeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommitteeService {

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private StudentActivityRepository studentActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommitteeMapper committeeMapper;

    public CommitteeResponseDTO createCommittee(CommitteeRequestDTO requestDTO, Long currentUserId) {
        // Validate activity exists
        StudentActivity activity = studentActivityRepository.findById(requestDTO.getActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity", requestDTO.getActivityId()));

        // Check if current user is the president of the activity
        if (!activity.getPresident().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the president can create committees for this activity");
        }

        // Validate head user exists
        User headUser = userRepository.findById(requestDTO.getHeadId())
                .orElseThrow(() -> new ResourceNotFoundException("Committee Head", requestDTO.getHeadId()));

        // Validate director user exists if provided
        User directorUser = null;
        if (requestDTO.getDirectorId() != null) {
            directorUser = userRepository.findById(requestDTO.getDirectorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Committee Director", requestDTO.getDirectorId()));
        }

        // Create committee
        Committee committee = new Committee();
        committee.setName(requestDTO.getName());
        committee.setDescription(requestDTO.getDescription());
        committee.setActivity(activity);
        committee.setHead(headUser);
        committee.setDirector(directorUser);

        // Save committee
        Committee savedCommittee = committeeRepository.save(committee);

        return committeeMapper.toResponseDTO(savedCommittee);
    }

    public CommitteeResponseDTO assignCommitteeHead(Long committeeId, Long headUserId, Long currentUserId) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));

        // Check if current user is the president of the activity
        if (!committee.getActivity().getPresident().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the president can assign committee heads");
        }

        // Validate head user exists
        User headUser = userRepository.findById(headUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", headUserId));

        // Assign head
        committee.setHead(headUser);
        Committee savedCommittee = committeeRepository.save(committee);

        return committeeMapper.toResponseDTO(savedCommittee);
    }

    public CommitteeResponseDTO assignCommitteeDirector(Long committeeId, Long directorUserId, Long currentUserId) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));

        // Check if current user is the president of the activity
        if (!committee.getActivity().getPresident().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the president can assign committee directors");
        }

        // Validate director user exists
        User directorUser = userRepository.findById(directorUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", directorUserId));

        // Assign director
        committee.setDirector(directorUser);
        Committee savedCommittee = committeeRepository.save(committee);

        return committeeMapper.toResponseDTO(savedCommittee);
    }

    public List<CommitteeResponseDTO> getCommitteesByActivity(Long activityId) {
        StudentActivity activity = studentActivityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));

        List<Committee> committees = committeeRepository.findByActivity(activity);
        return committees.stream()
                .map(committeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public CommitteeResponseDTO getCommitteeById(Long id) {
        Committee committee = committeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", id));
        return committeeMapper.toResponseDTO(committee);
    }

    public List<CommitteeResponseDTO> getCommitteesByDirector(Long directorId) {
        List<Committee> committees = committeeRepository.findByDirectorId(directorId);
        return committees.stream()
                .map(committeeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public int countCommitteeMembers(Long committeeId) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));
        return committee.getMembers().size();
    }

    public List<UserResponseDTO> getCommitteeMembers(Long committeeId) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));
        
        return committee.getMembers().stream()
                .map(member -> {
                    UserResponseDTO dto = new UserResponseDTO();
                    dto.setId(member.getId());
                    dto.setFullName(member.getFullName());
                    dto.setEmail(member.getEmail());
                    dto.setCollageId(member.getCollageId());
                    dto.setGrade(member.getGrade());
                    dto.setMajor(member.getMajor());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public int countCommitteesInActivity(Long activityId) {
        StudentActivity activity = studentActivityRepository.findById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity", activityId));
        List<Committee> committees = committeeRepository.findByActivity(activity);
        return committees.size();
    }

    public void deleteCommittee(Long committeeId, Long currentUserId) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));

        // Check if current user is the president of the activity
        if (!committee.getActivity().getPresident().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the president can delete committees");
        }

        committeeRepository.delete(committee);
    }

    public CommitteeResponseDTO addMember(Long committeeId, AddMemberRequestDTO requestDTO, Long currentUserId) {
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));

        // Check if current user is the committee head
        if (committee.getHead() == null || !committee.getHead().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the committee head can add members");
        }

        // Find the student to add
        User student = userRepository.findByIdAndEnabledTrue(requestDTO.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Enabled Student", requestDTO.getStudentId()));

        // Check if student is already a member
        if (committee.getMembers().contains(student)) {
            throw new ForbiddenException("Student is already a member of this committee");
        }

        // Add student to committee
        committee.getMembers().add(student);
        Committee savedCommittee = committeeRepository.save(committee);

        return committeeMapper.toResponseDTO(savedCommittee);
    }

    public List<User> searchStudents(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }
        
        // Try to parse as ID first
        try {
            Long studentId = Long.parseLong(searchTerm.trim());
            return userRepository.findByIdAndEnabledTrue(studentId)
                    .map(List::of)
                    .orElse(List.of());
        } catch (NumberFormatException e) {
            // Search by name if not a valid ID
            return userRepository.searchByName(searchTerm.trim());
        }
    }

    public List<User> getAllStudents() {
        return userRepository.findByEnabledTrue();
    }

    public List<CommitteeResponseDTO> getPresidentCommittees(Long presidentId) {
        // Get all committees and filter by activity president
        List<Committee> allCommittees = committeeRepository.findAll();
        java.util.List<Committee> presidentCommittees = new java.util.ArrayList<>();
        
        for (Committee committee : allCommittees) {
            try {
                // Check if the activity's president matches the current user
                if (committee.getActivity() != null && 
                    committee.getActivity().getPresident() != null &&
                    committee.getActivity().getPresident().getId().equals(presidentId)) {
                    presidentCommittees.add(committee);
                }
            } catch (Exception e) {
                // Skip committees with loading issues
                continue;
            }
        }
        
        // Convert to DTOs
        return presidentCommittees.stream()
                .map(committee -> committeeMapper.toResponseDTO(committee))
                .collect(java.util.stream.Collectors.toList());
    }

    public CommitteeResponseDTO updateCommittee(Long committeeId, CommitteeRequestDTO requestDTO, Long currentUserId) {
        // Validate committee exists
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));

        // Check if current user is the president of the activity
        if (!committee.getActivity().getPresident().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the president can update committees for this activity");
        }

        // Validate head user exists if provided
        if (requestDTO.getHeadId() != null) {
            User headUser = userRepository.findById(requestDTO.getHeadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Committee Head", requestDTO.getHeadId()));
            committee.setHead(headUser);
        }

        // Validate director user exists if provided
        if (requestDTO.getDirectorId() != null) {
            User directorUser = userRepository.findById(requestDTO.getDirectorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Committee Director", requestDTO.getDirectorId()));
            committee.setDirector(directorUser);
        } else {
            committee.setDirector(null); // Allow removing director
        }

        // Update committee details
        committee.setName(requestDTO.getName());
        committee.setDescription(requestDTO.getDescription());

        // Save committee
        Committee savedCommittee = committeeRepository.save(committee);

        return committeeMapper.toResponseDTO(savedCommittee);
    }

    public void removeMemberFromCommittee(Long committeeId, Long memberId, Long currentUserId) {
        // Validate committee exists
        Committee committee = committeeRepository.findById(committeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Committee", committeeId));

        // Check if current user is the head of the committee
        if (committee.getHead() == null || !committee.getHead().getId().equals(currentUserId)) {
            throw new ForbiddenException("Only the committee head can remove members from this committee");
        }

        // Validate member exists
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member", memberId));

        // Check if the member is the head of the committee (prevent self-removal)
        if (committee.getHead() != null && committee.getHead().getId().equals(memberId)) {
            throw new ForbiddenException("Cannot remove the committee head. Please assign a new head first.");
        }

        // Check if the member is the director of the committee
        if (committee.getDirector() != null && committee.getDirector().getId().equals(memberId)) {
            committee.setDirector(null);
            committeeRepository.save(committee);
        }

        // Remove the member from the committee's members list
        committee.getMembers().remove(member);
        committeeRepository.save(committee);
        
        // Log the removal action
        System.out.println("Member " + memberId + " removed from committee " + committeeId + " by head " + currentUserId);
    }
}
