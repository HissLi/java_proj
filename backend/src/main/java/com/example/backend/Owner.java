package com.example.backend;
import lombok.Data;
@Data
public class Owner {
    private int reputation;
    private int accept_rate;
    private int user_id;
    private String user_type;
    private String profile_image;
    private String display_name;
    private String link;

    // Getters and setters for all fields
    // ...
}
