package com.example.bookPortal.projection;

import com.example.bookPortal.entity.Book;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Projection(name = "bookView", types = Book.class)
public interface BookProjection {

    Integer getBookId();

    String getTitle();

    String getIsbn();

    String getDescription();

    LocalDate getPublishedDate();

    String getLanguage();

    Integer getPages();

    String getCoverImage();

    LocalDateTime getCreatedAt();

    PublisherInfo getPublisher();

    interface PublisherInfo {
        Integer getPublisherId();
        String getPublisherName();
        String getCity();
        String getState();
        String getCountry();
        String getWebsite();
    }
}