# News Aggregator

## Overview

The News Aggregator is a Spring Boot application that provides a RESTful API for managing user news preferences and fetching news articles from various external news APIs. The application supports user registration, login, and token-based authentication using JWT. Users can manage their news preferences, mark articles as read or favorite, and search for news articles based on keywords.

## Features

- **User Registration and Login**: Register and log in users with JWT-based authentication.
- **News Preferences Management**: Retrieve and update user-specific news preferences.
- **News Fetching**: Fetch news articles based on user preferences from external APIs.
- **Article Management**: Mark articles as read or favorite and retrieve lists of read or favorite articles.
- **Search Functionality**: Search for news articles based on keywords.
- **Caching**: Cache news articles to reduce external API calls.
- **Periodic Updates**: Automatically update cached news articles in the background.
- **Swagger Documentation**: Interactive API documentation for easy exploration and testing.

## Endpoints

### Authentication

- **POST /api/register**
  - Register a new user.
  - **Request Body**: `RegisterRequest` (includes username, password, etc.)
  - **Response**: `AuthResponse`

- **POST /api/login**
  - Log in a user.
  - **Request Body**: `AuthRequest` (includes username and password)
  - **Response**: `AuthResponse` (includes JWT token)

### News Preferences

- **GET /api/preferences**
  - Retrieve news preferences for the logged-in user.
  - **Response**: `NewsPreferencesResponse`

- **PUT /api/preferences**
  - Update news preferences for the logged-in user.
  - **Request Body**: `UpdateNewsPreferencesRequest`
  - **Response**: `NewsPreferencesResponse`

### News Management

- **GET /api/news**
  - Fetch news articles based on user preferences.
  - **Response**: JSON array of news articles

- **POST /api/news/{id}/read**
  - Mark a news article as read.
  - **Path Variable**: `id` (article ID)

- **POST /api/news/{id}/favorite**
  - Mark a news article as favorite.
  - **Path Variable**: `id` (article ID)

- **GET /api/news/read**
  - Retrieve all read news articles.
  - **Response**: JSON array of read articles

- **GET /api/news/favorites**
  - Retrieve all favorite news articles.
  - **Response**: JSON array of favorite articles

- **GET /api/news/search/{keyword}**
  - Search for news articles based on keywords.
  - **Path Variable**: `keyword` (search keyword)
  - **Response**: JSON array of news articles

## External News APIs

The application integrates with the following external news APIs:

- **GNews API**: [GNews Documentation](https://gnews.io/docs/)

*Note: You will need to sign up for API keys for these services.*
*Add your API keys to the application properties file (src/main/resources/application.properties).*


## Setup and Installation

1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd news-aggregator
   ```
2. **Build the Project**:
   ```bash
   ./gradlew build
   ```
3. **Run the Application**:
   ```
   ./gradlew bootRun
   ```

## Swagger Documentation

Swagger is integrated for interactive API documentation. You can explore and test the API endpoints using the Swagger UI at: http://localhost:8080/swagger-ui/index.html 

