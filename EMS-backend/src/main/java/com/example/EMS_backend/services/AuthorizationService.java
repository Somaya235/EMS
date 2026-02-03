package com.example.EMS_backend.services;

import com.example.EMS_backend.models.*;
import com.example.EMS_backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentActivityRepository studentActivityRepository;

    @Autowired
    private CommitteeRepository committeeRepository;

    @Autowired
    private CommitteeMemberRepository committeeMemberRepository;

    @Autowired
    private ActivityDirectorRepository activityDirectorRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private PollRepository pollRepository;

    /**
     * Check if user has ADMIN role.
     */
    public boolean isAdmin(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getRole() == UserRole.ADMIN)
                .orElse(false);
    }

    /**
     * Check if user is president of the activity.
     */
    public boolean isActivityPresident(Long userId, Long activityId) {
        return studentActivityRepository.findById(activityId)
                .map(activity -> activity.getPresident() != null && activity.getPresident().getId().equals(userId))
                .orElse(false);
    }

    /**
     * Check if user is head of the committee.
     */
    public boolean isCommitteeHead(Long userId, Long committeeId) {
        return committeeMemberRepository.findByCommitteeId(committeeId).stream()
                .anyMatch(cm -> cm.getMember().getId().equals(userId) && cm.getHeadMember() != null && cm.getHeadMember().getId().equals(userId));
    }

    /**
     * Check if user is a member of the committee.
     */
    public boolean isCommitteeMember(Long userId, Long committeeId) {
        return committeeMemberRepository.existsById(new CommitteeMemberId(committeeId, userId));
    }

    /**
     * Check if user is director of the committee.
     */
    public boolean isCommitteeDirector(Long userId, Long committeeId) {
        return committeeRepository.findById(committeeId)
                .map(committee -> committee.getDirector() != null && committee.getDirector().getId().equals(userId))
                .orElse(false);
    }

    /**
     * Check if user is web manager of the activity.
     */
    public boolean isWebManager(Long userId, Long activityId) {
        return studentActivityRepository.findById(activityId)
                .map(activity -> activity.getWebManager() != null && activity.getWebManager().getId().equals(userId))
                .orElse(false);
    }

    /**
     * Check if user is a director of the activity.
     */
    public boolean isActivityDirector(Long userId, Long activityId) {
        return activityDirectorRepository.findById(new ActivityDirectorId(activityId, userId)).isPresent();
    }

    /**
     * Combined check for committee management (president of activity or head of committee).
     */
    public boolean canManageCommittee(Long userId, Long committeeId) {
        if (isAdmin(userId)) return true;
        
        Optional<Committee> committeeOpt = committeeRepository.findById(committeeId);
        if (committeeOpt.isPresent()) {
            Committee committee = committeeOpt.get();
            Long activityId = committee.getActivity().getId();
            return isActivityPresident(userId, activityId) || isCommitteeHead(userId, committeeId);
        }
        return false;
    }

    /**
     * Combined check for activity management (president of activity or admin).
     */
    public boolean canManageActivity(Long userId, Long activityId) {
        return isAdmin(userId) || isActivityPresident(userId, activityId);
    }

    /**
     * Check if user can create an event in an activity.
     */
    public boolean canCreateEventInActivity(Long userId, Long activityId) {
        return canManageActivity(userId, activityId) || isActivityDirector(userId, activityId) || isWebManager(userId, activityId);
    }

    /**
     * Check if user can create an event in a committee.
     */
    public boolean canCreateEventInCommittee(Long userId, Long committeeId) {
        if (canManageCommittee(userId, committeeId)) return true;
        
        return committeeRepository.findById(committeeId)
                .map(committee -> isCommitteeDirector(userId, committeeId))
                .orElse(false);
    }

    /**
     * Combined check for event management.
     */
    public boolean canManageEvent(Long userId, Long eventId) {
        if (isAdmin(userId)) return true;

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isPresent()) {
            Event event = eventOpt.get();
            if (event.getCommittee() != null) {
                if (canManageCommittee(userId, event.getCommittee().getId())) return true;
            }
            if (event.getActivity() != null) {
                if (canManageActivity(userId, event.getActivity().getId())) return true;
                if (isActivityDirector(userId, event.getActivity().getId())) return true;
                if (isWebManager(userId, event.getActivity().getId())) return true;
            }
        }
        return false;
    }

    /**
     * Check if user can create a poll in an event.
     */
    public boolean canCreatePollInEvent(Long userId, Long eventId) {
        return canManageEvent(userId, eventId);
    }

    /**
     * Combined check for poll management.
     */
    public boolean canManagePoll(Long userId, Long pollId) {
        if (isAdmin(userId)) return true;

        return pollRepository.findById(pollId)
                .map(poll -> canManageEvent(userId, poll.getEvent().getId()))
                .orElse(false);
    }
}
