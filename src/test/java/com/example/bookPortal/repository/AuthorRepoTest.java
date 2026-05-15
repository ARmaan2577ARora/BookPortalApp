package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Author;
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

    @Test
    void testAuthorRepoMethods() {
        String suffix = RepoTestHelper.suffix();

        Author author = authorRepo.save(RepoTestHelper.author(suffix));

        assertThat(authorRepo.findByFirstNameContainingIgnoreCase(suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());

        assertThat(authorRepo.findByLastNameContainingIgnoreCase(suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());

        assertThat(authorRepo.findByCountryIgnoreCase(author.getCountry().toLowerCase()))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());

        Optional<Author> byPhone = authorRepo.findByPhone(author.getPhone());
        assertThat(byPhone).isPresent();

        assertThat(authorRepo.existsByPhone(author.getPhone())).isTrue();

        Optional<Author> byFullName = authorRepo.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                author.getFirstName().toLowerCase(),
                author.getLastName().toLowerCase()
        );
        assertThat(byFullName).isPresent();

        assertThat(authorRepo.findByFirstNameStartingWithIgnoreCase("First_" + suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());

        assertThat(authorRepo.findByLastNameStartingWithIgnoreCase("Last_" + suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());

        assertThat(authorRepo.findByBioContainingIgnoreCase(suffix))
                .extracting(Author::getAuthorId)
                .contains(author.getAuthorId());
    }
}