package com.example.entity;

import jakarta.persistence.PrePersist;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class Comment {

    private Long id;
    private Post post;
    private String userName;
    private Long userId;
    private String message;
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreated() {
        this.createdDate = LocalDateTime.now();
    }

}
