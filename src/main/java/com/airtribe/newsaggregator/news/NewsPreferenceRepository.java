package com.airtribe.newsaggregator.news;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airtribe.newsaggregator.user.User;

public interface NewsPreferenceRepository extends JpaRepository<NewsPreference, Long> {
    NewsPreference findByUser(User user);
}