package com.example.EMS_backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse {
    
    @JsonProperty("message")
    private String message;

    public MessageResponse() {
    }

    public MessageResponse(String message) {
        this.message = message;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
}
