package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Book;
import com.example.bookPortal.projection.BookProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "books", excerptProjection = BookProjection.class)
public interface BookRepo extends JpaRepository<Book, Integer> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByIsbnContainingIgnoreCase(String isbn);

    List<Book> findByLanguageIgnoreCase(String language);

    List<Book> findByPublisher_PublisherNameContainingIgnoreCase(String publisherName);

    Optional<Book> findByIsbn(String isbn);

    boolean existsByIsbn(String isbn);

    List<Book> findByPagesBetween(Integer minPages, Integer maxPages);

    List<Book> findByPublishedDateBetween(LocalDate startDate, LocalDate endDate);

    List<Book> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Book> findByPublisher_PublisherId(Integer publisherId);

    List<Book> findByPublisher_CountryIgnoreCase(String country);

    List<BookProjection> findBookProjectionByTitleContainingIgnoreCase(String title);

    List<BookProjection> findBookProjectionByIsbnContainingIgnoreCase(String isbn);

    List<BookProjection> findBookProjectionByLanguageIgnoreCase(String language);

    List<BookProjection> findBookProjectionByPublisher_PublisherId(Integer publisherId);

    List<BookProjection> findBookProjectionByPublisher_PublisherNameContainingIgnoreCase(String publisherName);
}