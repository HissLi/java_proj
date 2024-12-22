package com.example.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
@Data
public class Answer {
    private List<String> tags;
    private Owner owner;
    private int score;
    private int last_activity_date;
    private int last_edit_date;
    private int creation_date;
    private int community_owned_date;
    private int answer_id;
    private int question_id;
    private String link;
    private String title;
    private String body;
    @JsonProperty("is_accepted")
    private boolean is_accepted;
}
