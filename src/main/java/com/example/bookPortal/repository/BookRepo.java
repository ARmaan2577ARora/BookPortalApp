package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Integer> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByLanguageIgnoreCase(String language);

    List<Book> findByPagesBetween(Integer minPages, Integer maxPages);

    List<Book> findByPublishedDateBetween(LocalDate startDate, LocalDate endDate);

    List<Book> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Book> findByPublisher_PublisherId(Integer publisherId);

    List<Book> findByPublisher_PublisherNameContainingIgnoreCase(String publisherName);

    List<Book> findByPublisher_CountryIgnoreCase(String country);
}