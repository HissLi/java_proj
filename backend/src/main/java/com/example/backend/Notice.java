package com.example.backend;
import lombok.Data;
@Data
public class Notice {
    private int owner_user_id;
    private int creation_date;
    private String body;
}
