package com.example.backend;
import lombok.Data;
@Data
public class Comment {
    private Owner owner;
    private boolean edited;
    private int score;
    private int creation_date;
    private int post_id;
    private int comment_id;
    private String body;
    private Owner reply_to_user;
    // Getters and setters for all fields
    // ...
}
