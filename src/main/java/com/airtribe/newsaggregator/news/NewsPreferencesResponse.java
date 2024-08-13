package com.airtribe.newsaggregator.news;

import lombok.Data;

@Data
public class NewsPreferencesResponse {
    private Long id;
    private String category;
    private String language;
    private String country;
}

