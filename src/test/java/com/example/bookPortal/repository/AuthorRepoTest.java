package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepoTest {

    @Autowired
    private AuthorRepo authorRepo;

    private Author author;
    private String suffix;

    @BeforeEach
    void setUp() {
        suffix = RepoTestHelper.suffix();
        author = authorRepo.save(RepoTestHelper.author(suffix));
    }

    @Test
    void shouldFindAuthorByFirstNameContainingIgnoreCase() {
        assertThat(authorRepo.findByFirstNameContainingIgnoreCase(suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());
    }

    @Test
    void shouldFindAuthorByLastNameContainingIgnoreCase() {
        assertThat(authorRepo.findByLastNameContainingIgnoreCase(suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());
    }

    @Test
    void shouldFindAuthorByCountryIgnoreCase() {
        assertThat(authorRepo.findByCountryIgnoreCase(author.getCountry().toLowerCase()))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());
    }

    @Test
    void shouldFindAuthorByPhone() {
        Optional<Author> byPhone = authorRepo.findByPhone(author.getPhone());

        assertThat(byPhone).isPresent();
        assertThat(byPhone.get().getAuthorId()).isEqualTo(author.getAuthorId());
    }

    @Test
    void shouldCheckIfAuthorExistsByPhone() {
        assertThat(authorRepo.existsByPhone(author.getPhone()))
                .isTrue();
    }

    @Test
    void shouldFindAuthorByFirstAndLastNameIgnoreCase() {
        Optional<Author> result =
                authorRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                        author.getFirstName().toLowerCase(),
                        author.getLastName().toLowerCase()
                );

        assertThat(result).isPresent();
        assertThat(result.get().getAuthorId()).isEqualTo(author.getAuthorId());
    }

    @Test
    void shouldFindAuthorByFirstNameStartingWithIgnoreCase() {
        assertThat(authorRepo.findByFirstNameStartingWithIgnoreCase("First_" + suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());
    }

    @Test
    void shouldFindAuthorByLastNameStartingWithIgnoreCase() {
        assertThat(authorRepo.findByLastNameStartingWithIgnoreCase("Last_" + suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());
    }

    @Test
    void shouldFindAuthorByBioContainingIgnoreCase() {
        assertThat(authorRepo.findByBioContainingIgnoreCase(suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());
    }
}