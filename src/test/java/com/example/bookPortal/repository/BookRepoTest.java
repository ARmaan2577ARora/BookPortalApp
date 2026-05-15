package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Book;
import com.example.bookPortal.entity.Publisher;
import org.junit.jupiter.api.BeforeEach;
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

    private Publisher publisher;
    private Book book;

    @BeforeEach
    void setUp() {
        String suffix = RepoTestHelper.suffix();

        publisher = publisherRepo.save(
                RepoTestHelper.publisher(suffix)
        );

        book = bookRepo.save(
                RepoTestHelper.book(suffix, publisher)
        );
    }

    @Test
    void shouldFindBookByTitleContainingIgnoreCase() {

        assertThat(bookRepo.findByTitleContainingIgnoreCase(book.getTitle()))
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }

    @Test
    void shouldFindBookByIsbn() {

        Optional<Book> byIsbn = bookRepo.findByIsbn(book.getIsbn());

        assertThat(byIsbn).isPresent();
        assertThat(byIsbn.get().getBookId()).isEqualTo(book.getBookId());
    }

    @Test
    void shouldCheckIfBookExistsByIsbn() {

        assertThat(bookRepo.existsByIsbn(book.getIsbn()))
                .isTrue();
    }

    @Test
    void shouldFindBooksByLanguageIgnoreCase() {

        assertThat(
                bookRepo.findByLanguageIgnoreCase(
                        book.getLanguage().toLowerCase()
                )
        )
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }

    @Test
    void shouldFindBooksByPagesBetween() {

        assertThat(bookRepo.findByPagesBetween(100, 300))
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }

    @Test
    void shouldFindBooksByPublishedDateBetween() {

        assertThat(
                bookRepo.findByPublishedDateBetween(
                        LocalDate.now().minusDays(1),
                        LocalDate.now().plusDays(1)
                )
        )
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }

    @Test
    void shouldFindBooksByCreatedAtBetween() {

        assertThat(
                bookRepo.findByCreatedAtBetween(
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(1)
                )
        )
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }

    @Test
    void shouldFindBooksByPublisherId() {

        assertThat(
                bookRepo.findByPublisher_PublisherId(
                        publisher.getPublisherId()
                )
        )
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }

    @Test
    void shouldFindBooksByPublisherNameContainingIgnoreCase() {

        assertThat(
                bookRepo.findByPublisher_PublisherNameContainingIgnoreCase(
                        publisher.getPublisherName()
                )
        )
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }

    @Test
    void shouldFindBooksByPublisherCountryIgnoreCase() {

        assertThat(
                bookRepo.findByPublisher_CountryIgnoreCase(
                        publisher.getCountry().toLowerCase()
                )
        )
                .extracting(Book::getBookId)
                .contains(book.getBookId());
    }
}