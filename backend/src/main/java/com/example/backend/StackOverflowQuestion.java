package com.example.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;
@Data
public class StackOverflowQuestion {
    private MigratedFrom migrated_from;
    private Notice notice;
    private List<String> tags;
    private List<Comment> comments;
    private List<Answer> answers;
    private Owner owner;
    private int delete_vote_count;
    private int reopen_vote_count;
    private int close_vote_count;
    @JsonProperty("is_answered")
    private boolean is_answered;
    private int view_count;
    private int favorite_count;
    private int answer_count;
    private int score;
    private int last_activity_date;
    private int creation_date;
    private int protected_date;
    private int closed_date;
    private String closed_reason;
    private int last_edit_date;
    private int question_id;
    private int accepted_answer_id;
    private int bounty_amount;
    private int bounty_closes_date;
    private int community_owned_date;
    private int locked_date;
    private String link;
    private String title;
    @JsonDeserialize(using = BodyDeserializer.class)
    private String body;
}

