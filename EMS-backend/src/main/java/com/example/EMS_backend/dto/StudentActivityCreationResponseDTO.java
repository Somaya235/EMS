package com.example.EMS_backend.dto;

/**
 * Success response for creating a student activity and assigning its president.
 *
 * {
 *   "status": "SUCCESS",
 *   "message": "Student activity created and president assigned successfully",
 *   "data": {
 *     "activityId": 5,
 *     "presidentId": 12
 *   }
 * }
 */
public class StudentActivityCreationResponseDTO {

    private String status;
    private String message;
    private Data data;

    public StudentActivityCreationResponseDTO() {
    }

    public StudentActivityCreationResponseDTO(String status, String message, Long activityId, Long presidentId) {
        this.status = status;
        this.message = message;
        this.data = new Data(activityId, presidentId);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private Long activityId;
        private Long presidentId;

        public Data() {
        }

        public Data(Long activityId, Long presidentId) {
            this.activityId = activityId;
            this.presidentId = presidentId;
        }

        public Long getActivityId() {
            return activityId;
        }

        public void setActivityId(Long activityId) {
            this.activityId = activityId;
        }

        public Long getPresidentId() {
            return presidentId;
        }

        public void setPresidentId(Long presidentId) {
            this.presidentId = presidentId;
        }
    }
}

