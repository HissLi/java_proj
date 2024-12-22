package com.example.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class Answer {
    public List<String> tags;
    public Owner owner;
    public int score;
    public int last_activity_date;
    public int last_edit_date;
    public int creation_date;
    public int community_owned_date;
    public int answer_id;
    public int question_id;
    public String link;
    public String title;
    public String body;
    @JsonProperty("is_accepted")
    public boolean is_accepted;
}
