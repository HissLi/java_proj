package com.example.backend;

import lombok.Data;

import java.util.List;

@Data
public class OtherSite {
    public List<String> markdown_extensions;
    public long launch_date;
    public long open_beta_date;
    public String site_state;
    public String high_resolution_icon_url;
    public String favicon_url;
    public String icon_url;
    public String audience;
    public String site_url;
    public String api_site_parameter;
    public String logo_url;
    public String name;
    public String site_type;
}