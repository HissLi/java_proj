package com.example.backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;
@Data
public class StackOverflowQuestion {
    public MigratedFrom migrated_from;
    public Notice notice;
    public List<String> tags;
    public List<Comment> comments;
    public List<Answer> answers;
    public Owner owner;
    public int delete_vote_count;
    public int reopen_vote_count;
    public int close_vote_count;
    @JsonProperty("is_answered")
    public boolean is_answered;
    public int view_count;
    public int favorite_count;
    public int answer_count;
    public int score;
    public int last_activity_date;
    public int creation_date;
    public int protected_date;
    public int closed_date;
    public String closed_reason;
    public int last_edit_date;
    public int question_id;
    public int accepted_answer_id;
    public int bounty_amount;
    public int bounty_closes_date;
    public int community_owned_date;
    public int locked_date;
    public String link;
    public String title;
    @JsonDeserialize(using = BodyDeserializer.class)
    public String body;
}
