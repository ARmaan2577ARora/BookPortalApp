package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublisherRepo extends JpaRepository<Publisher, Integer> {

    List<Publisher> findByPublisherNameContainingIgnoreCase(String publisherName);

    Optional<Publisher> findByPublisherNameIgnoreCase(String publisherName);

    List<Publisher> findByCityIgnoreCase(String city);

    List<Publisher> findByStateIgnoreCase(String state);

    List<Publisher> findByCountryIgnoreCase(String country);

    Optional<Publisher> findByWebsite(String website);

    boolean existsByWebsite(String website);
}