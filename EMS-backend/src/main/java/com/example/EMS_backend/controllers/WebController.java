package com.example.EMS_backend.controllers;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@RestController
public class WebController {

    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPasswordPage(@RequestParam(required = false) String token) throws IOException {
        Resource resource = new ClassPathResource("static/reset-password.html");
        String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
        
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(content);
    }
}
