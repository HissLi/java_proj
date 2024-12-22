package com.example.backend;
import lombok.Data;
@Data
public class Comment {
    public Owner owner;
    public boolean edited;
    public int score;
    public int creation_date;
    public int post_id;
    public int comment_id;
    public String body;
    public Owner reply_to_user;
    // Getters and setters for all fields
    // ...
}
