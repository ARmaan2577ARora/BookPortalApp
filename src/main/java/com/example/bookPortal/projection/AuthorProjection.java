package com.example.bookPortal.projection;

import com.example.bookPortal.entity.Author;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "authorView", types = Author.class)
public interface AuthorProjection {

    Integer getAuthorId();

    String getFirstName();

    String getLastName();

    String getBio();

    String getPhone();

    String getCountry();
}