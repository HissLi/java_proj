package com.example.backend;

import lombok.Data;

import java.util.List;

@Data
public class OtherSite {
    private List<String> markdown_extensions;
    private long launch_date;
    private long open_beta_date;
    private String site_state;
    private String high_resolution_icon_url;
    private String favicon_url;
    private String icon_url;
    private String audience;
    private String site_url;
    private String api_site_parameter;
    private String logo_url;
    private String name;
    private String site_type;
}