package com.airtribe.newsaggregator.news;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @Operation(summary = "Get user news preferences", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved preferences"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @GetMapping("/preferences")
    public ResponseEntity<NewsPreferencesResponse> getPreferences(@AuthenticationPrincipal UserDetails userDetails) {
        NewsPreferencesResponse preferences = newsService.getNewsPreferences(userDetails.getUsername());
        return ResponseEntity.ok(preferences);
    }

    @Operation(summary = "Update user news preferences", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/preferences")
    public ResponseEntity<NewsPreferencesResponse> updatePreferences(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateNewsPreferencesRequest updateRequest) {
        NewsPreferencesResponse updatedPreferences = newsService.updateNewsPreferences(userDetails.getUsername(), updateRequest);
        return ResponseEntity.ok(updatedPreferences);
    }

    @Operation(summary = "Fetch news based on user preferences", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/news")
    public ResponseEntity<String> getNews(@AuthenticationPrincipal UserDetails userDetails) {
        String news = newsService.fetchNews(userDetails.getUsername());
        return ResponseEntity.ok(news);
    }

    @Operation(summary = "Search news by keyword", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/news/search/{keyword}")
    public ResponseEntity<String> searchNews(@PathVariable String keyword) {
        String news = newsService.searchNewsByKeyword(keyword);
        return ResponseEntity.ok(news);
    }

    @Operation(summary = "Mark an article as read", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/news/{id}/read")
    public ResponseEntity<Void> markArticleAsRead(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {
        newsService.markArticleAsRead(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Mark an article as favorite", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/news/{id}/favorite")
    public ResponseEntity<Void> markArticleAsFavorite(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id) {
        newsService.markArticleAsFavorite(userDetails.getUsername(), id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all read articles", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/news/read")
    public ResponseEntity<Set<String>> getReadArticles(@AuthenticationPrincipal UserDetails userDetails) {
        Set<String> readArticles = newsService.getReadArticles(userDetails.getUsername());
        return ResponseEntity.ok(readArticles);
    }

    @Operation(summary = "Get all favorite articles", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/news/favorites")
    public ResponseEntity<Set<String>> getFavoriteArticles(@AuthenticationPrincipal UserDetails userDetails) {
        Set<String> favoriteArticles = newsService.getFavoriteArticles(userDetails.getUsername());
        return ResponseEntity.ok(favoriteArticles);
    }
}
