package com.example.bookPortal.projection;

import com.example.bookPortal.entity.Publisher;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "publisherView", types = Publisher.class)
public interface PublisherProjection {

    Integer getPublisherId();

    String getPublisherName();

    String getCity();

    String getState();

    String getCountry();

    String getWebsite();
}