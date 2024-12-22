package com.example.backend;

import lombok.Data;

@Data
public class MigratedFrom {
    public OtherSite other_site;
    public long on_date;
    public int question_id;
}