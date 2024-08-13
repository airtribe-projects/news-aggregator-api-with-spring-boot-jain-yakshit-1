package com.airtribe.newsaggregator.news;

import lombok.Data;

@Data
public class UpdateNewsPreferencesRequest {
    private String category;
    private String language;
    private String country;
}