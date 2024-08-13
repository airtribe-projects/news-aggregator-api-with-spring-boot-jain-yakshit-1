package com.airtribe.newsaggregator.news;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import com.airtribe.newsaggregator.user.User;
import com.airtribe.newsaggregator.user.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NewsService {
    
    @Value("${gnews.api.key}")
    private String apiKey;

    private final NewsPreferenceRepository newsPreferenceRepository;
    private final UserRepository userRepository;
    private final RestClient restClient;
    private final NewsPreferenceMapper newsPreferenceMapper = NewsPreferenceMapper.INSTANCE;


    public NewsPreferencesResponse getNewsPreferences(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        NewsPreference preferences = newsPreferenceRepository.findByUser(user);
        return newsPreferenceMapper.toDTO(preferences);
    }

    @Transactional
    public void markArticleAsRead(String userEmail, String articleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.getReadArticles().add(articleId);
        userRepository.save(user);
    }

    public Set<String> getReadArticles(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getReadArticles();
    }

    @Transactional
    public void markArticleAsFavorite(String userEmail, String articleId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.getFavoriteArticles().add(articleId);
        userRepository.save(user);
    }

    public Set<String> getFavoriteArticles(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getFavoriteArticles();
    }

    @Transactional
    public NewsPreferencesResponse updateNewsPreferences(String userEmail, UpdateNewsPreferencesRequest updateRequest) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        NewsPreference preferences = newsPreferenceRepository.findByUser(user);
        if (preferences == null) {
            preferences = new NewsPreference();
            preferences.setUser(user);
        }
        preferences = newsPreferenceMapper.fromUpdateRequest(updateRequest);
        preferences.setUser(user);
        preferences = newsPreferenceRepository.save(preferences);
        return newsPreferenceMapper.toDTO(preferences);
    }

    @Cacheable(value = "newsCache", key = "#userEmail")
    public String fetchNews(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        NewsPreference preferences = newsPreferenceRepository.findByUser(user);
        String url = buildNewsApiUrl(preferences);
        return restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    @Scheduled(fixedRate = 3600000) // Update cache every hour
    @CacheEvict(value = "newsCache", allEntries = true)
    public void refreshNewsCache() {
        userRepository.findAll().forEach(user -> {
            String userEmail = user.getEmail();
            fetchNews(userEmail); // This will repopulate the cache with updated data
        });
    }

    @Cacheable(value = "newsSearchCache", key = "#keyword")
    public String searchNewsByKeyword(String keyword) {
        String url = String.format("https://gnews.io/api/v4/search?q=%s&apikey=%s", keyword, apiKey);
        return restClient
                .get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    private String buildNewsApiUrl(NewsPreference preferences) {
        StringBuilder url = new StringBuilder("https://gnews.io/api/v4/top-headlines?apikey=" + apiKey);
        if(preferences == null ){
            return url.toString();
        }

        if (preferences.getCategory() != null) {
            url.append("&category=").append(preferences.getCategory());
        }
        if (preferences.getCountry() != null) {
            url.append("&country=").append(preferences.getCountry());
        }
        if (preferences.getLanguage() != null) {
            url.append("&language=").append(preferences.getLanguage());
        }
        return url.toString();
    }
}
