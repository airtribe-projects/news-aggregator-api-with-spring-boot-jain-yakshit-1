package com.airtribe.newsaggregator.news;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsPreferenceMapper {

    NewsPreferenceMapper INSTANCE = Mappers.getMapper(NewsPreferenceMapper.class);

    // Map from NewsPreference to NewsPreferencesResponse
    @Mapping(source = "id", target = "id")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "language", target = "language")
    @Mapping(source = "country", target = "country")
    NewsPreferencesResponse toDTO(NewsPreference newsPreference);

    // Map from UpdateNewsPreferencesRequest to NewsPreference
    @Mapping(source = "category", target = "category")
    @Mapping(source = "language", target = "language")
    @Mapping(source = "country", target = "country")
    NewsPreference fromUpdateRequest(UpdateNewsPreferencesRequest updateRequest);
}


