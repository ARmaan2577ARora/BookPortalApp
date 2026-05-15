package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Book;
import com.example.bookPortal.entity.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepoTest {

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private PublisherRepo publisherRepo;

    @Test
    void testBookRepoMethods() {
        String suffix = RepoTestHelper.suffix();

        Publisher publisher = publisherRepo.save(RepoTestHelper.publisher(suffix));
        Book book = bookRepo.save(RepoTestHelper.book(suffix, publisher));

        assertThat(bookRepo.findByTitleContainingIgnoreCase(suffix))
                .extracting(Book::getBookId)
                .contains(book.getBookId());

        Optional<Book> byIsbn = bookRepo.findByIsbn(book.getIsbn());
        assertThat(byIsbn).isPresent();

        assertThat(bookRepo.existsByIsbn(book.getIsbn())).isTrue();

        assertThat(bookRepo.findByLanguageIgnoreCase(book.getLanguage().toLowerCase()))
                .extracting(Book::getBookId)
                .contains(book.getBookId());

        assertThat(bookRepo.findByPagesBetween(100, 300))
                .extracting(Book::getBookId)
                .contains(book.getBookId());

        assertThat(bookRepo.findByPublishedDateBetween(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)))
                .extracting(Book::getBookId)
                .contains(book.getBookId());

        assertThat(bookRepo.findByCreatedAtBetween(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)))
                .extracting(Book::getBookId)
                .contains(book.getBookId());

        assertThat(bookRepo.findByPublisher_PublisherId(publisher.getPublisherId()))
                .extracting(Book::getBookId)
                .contains(book.getBookId());

        assertThat(bookRepo.findByPublisher_PublisherNameContainingIgnoreCase(suffix))
                .extracting(Book::getBookId)
                .contains(book.getBookId());

        assertThat(bookRepo.findByPublisher_CountryIgnoreCase(publisher.getCountry().toLowerCase()))
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }
}