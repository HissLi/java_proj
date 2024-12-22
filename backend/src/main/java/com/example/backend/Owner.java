package com.example.backend;
import lombok.Data;
@Data
public class Owner {
    public int reputation;
    public int accept_rate;
    public int user_id;
    public String user_type;
    public String profile_image;
    public String display_name;
    public String link;

    // Getters and setters for all fields
    // ...
}
