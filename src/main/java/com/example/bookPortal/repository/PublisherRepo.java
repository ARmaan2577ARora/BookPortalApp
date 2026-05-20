package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.PublisherProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "publishers", excerptProjection = PublisherProjection.class)
public interface PublisherRepo extends JpaRepository<Publisher, Integer> {

    List<PublisherProjection> findPublisherProjectionByPublisherNameContainingIgnoreCase(String publisherName);

    Optional<PublisherProjection> findPublisherProjectionByPublisherNameIgnoreCase(String publisherName);

    List<PublisherProjection> findPublisherProjectionByCityIgnoreCase(String city);

    List<PublisherProjection> findPublisherProjectionByCountryIgnoreCase(String country);
    List<Publisher> findByPublisherNameContainingIgnoreCase(String publisherName);

    Optional<Publisher> findByPublisherNameIgnoreCase(String publisherName);

    List<Publisher> findByCityIgnoreCase(String city);

    List<Publisher> findByStateIgnoreCase(String state);

    List<Publisher> findByCountryIgnoreCase(String country);

    Optional<Publisher> findByWebsite(String website);

    boolean existsByWebsite(String website);
}