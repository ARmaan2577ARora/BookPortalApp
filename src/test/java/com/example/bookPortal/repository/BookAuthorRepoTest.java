package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Author;
import com.example.bookPortal.entity.Book;
import com.example.bookPortal.entity.BookAuthor;
import com.example.bookPortal.entity.BookAuthorId;
import com.example.bookPortal.entity.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookAuthorRepoTest {

    @Autowired
    private BookAuthorRepo bookAuthorRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private PublisherRepo publisherRepo;

    private String suffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private String phone() {
        long number = Math.abs(UUID.randomUUID().getMostSignificantBits());
        return "9" + String.format("%09d", number % 1_000_000_000L);
    }

    private Publisher createPublisher(String suffix) {
        Publisher publisher = new Publisher();
        publisher.setPublisherName("Publisher_" + suffix);
        publisher.setCity("City_" + suffix);
        publisher.setState("State_" + suffix);
        publisher.setCountry("Country_" + suffix);
        publisher.setWebsite("https://publisher" + suffix + ".com");
        return publisher;
    }

    private Book createBook(String suffix, Publisher publisher) {
        Book book = new Book();
        book.setTitle("Book_" + suffix);
        book.setIsbn("ISBN" + suffix);
        book.setDescription("Description_" + suffix);
        book.setPublishedDate(LocalDate.now());
        book.setLanguage("English");
        book.setPages(250);
        book.setCoverImage("cover_" + suffix + ".jpg");
        book.setCreatedAt(LocalDateTime.now());
        book.setPublisher(publisher);
        return book;
    }

    private Author createAuthor(String suffix) {
        Author author = new Author();
        author.setFirstName("First_" + suffix);
        author.setLastName("Last_" + suffix);
        author.setBio("Bio_" + suffix);
        author.setPhone(phone());
        author.setCountry("India");
        return author;
    }

    private BookAuthor createBookAuthor(Book book, Author author, Integer authorOrder) {
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);
        bookAuthor.setAuthorOrder(authorOrder);
        return bookAuthor;
    }

    @Test
    void findByBook_BookId_WhenBookExists_ReturnsBookAuthors() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        BookAuthor bookAuthor = bookAuthorRepo.save(createBookAuthor(book, author, 1));

        assertThat(bookAuthorRepo.findByBook_BookId(book.getBookId()))
                .extracting(BookAuthor::getId)
                .contains(bookAuthor.getId());
    }

    @Test
    void findByAuthor_AuthorId_WhenAuthorExists_ReturnsBookAuthors() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        BookAuthor bookAuthor = bookAuthorRepo.save(createBookAuthor(book, author, 1));

        assertThat(bookAuthorRepo.findByAuthor_AuthorId(author.getAuthorId()))
                .extracting(BookAuthor::getId)
                .contains(bookAuthor.getId());
    }

    @Test
    void findByBook_BookIdOrderByAuthorOrderAsc_WhenBookExists_ReturnsSortedBookAuthors() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));

        Author author1 = authorRepo.save(createAuthor("one_" + suffix));
        Author author2 = authorRepo.save(createAuthor("two_" + suffix));

        bookAuthorRepo.save(createBookAuthor(book, author1, 2));
        bookAuthorRepo.save(createBookAuthor(book, author2, 1));

        assertThat(bookAuthorRepo.findByBook_BookIdOrderByAuthorOrderAsc(book.getBookId()))
                .first()
                .extracting(BookAuthor::getAuthorOrder)
                .isEqualTo(1);
    }

    @Test
    void findByBook_BookIdAndAuthor_AuthorId_WhenExists_ReturnsBookAuthor() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        BookAuthor saved = bookAuthorRepo.save(createBookAuthor(book, author, 1));

        assertThat(bookAuthorRepo.findByBook_BookIdAndAuthor_AuthorId(
                book.getBookId(),
                author.getAuthorId()
        )).isPresent();

        BookAuthorId expectedId = new BookAuthorId(book.getBookId(), author.getAuthorId());

        assertThat(saved.getId()).isEqualTo(expectedId);
    }

    @Test
    void existsByBook_BookIdAndAuthor_AuthorId_WhenExists_ReturnsTrue() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        bookAuthorRepo.save(createBookAuthor(book, author, 1));

        boolean result = bookAuthorRepo.existsByBook_BookIdAndAuthor_AuthorId(
                book.getBookId(),
                author.getAuthorId()
        );

        assertThat(result).isTrue();
    }

    @Test
    void findByAuthorOrder_WhenAuthorOrderExists_ReturnsBookAuthor() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        BookAuthor bookAuthor = bookAuthorRepo.save(createBookAuthor(book, author, 1));

        assertThat(bookAuthorRepo.findByAuthorOrder(1))
                .extracting(BookAuthor::getId)
                .contains(bookAuthor.getId());
    }

    @Test
    void findByAuthor_FirstNameContainingIgnoreCase_WhenFirstNameMatches_ReturnsBookAuthor() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        BookAuthor bookAuthor = bookAuthorRepo.save(createBookAuthor(book, author, 1));

        assertThat(bookAuthorRepo.findByAuthor_FirstNameContainingIgnoreCase(suffix.toLowerCase()))
                .extracting(BookAuthor::getId)
                .contains(bookAuthor.getId());
    }

    @Test
    void findByAuthor_LastNameContainingIgnoreCase_WhenLastNameMatches_ReturnsBookAuthor() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        BookAuthor bookAuthor = bookAuthorRepo.save(createBookAuthor(book, author, 1));

        assertThat(bookAuthorRepo.findByAuthor_LastNameContainingIgnoreCase(suffix.toLowerCase()))
                .extracting(BookAuthor::getId)
                .contains(bookAuthor.getId());
    }

    @Test
    void findByBook_TitleContainingIgnoreCase_WhenTitleMatches_ReturnsBookAuthor() {
        String suffix = suffix();

        Publisher publisher = publisherRepo.save(createPublisher(suffix));
        Book book = bookRepo.save(createBook(suffix, publisher));
        Author author = authorRepo.save(createAuthor(suffix));

        BookAuthor bookAuthor = bookAuthorRepo.save(createBookAuthor(book, author, 1));

        assertThat(bookAuthorRepo.findByBook_TitleContainingIgnoreCase(suffix.toLowerCase()))
                .extracting(BookAuthor::getId)
                .contains(bookAuthor.getId());
    }
}