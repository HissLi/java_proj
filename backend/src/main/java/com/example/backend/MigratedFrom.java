package com.example.backend;

import lombok.Data;

@Data
public class MigratedFrom {
    private OtherSite other_site;
    private long on_date;
    private int question_id;
}