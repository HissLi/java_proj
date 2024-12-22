package com.example.backend;
import lombok.Data;
@Data
public class Notice {
    public int owner_user_id;
    public int creation_date;
    public String body;
}
