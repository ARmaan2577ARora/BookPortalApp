package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Publisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PublisherRepoTest {

    @Autowired
    private PublisherRepo publisherRepo;

    private Publisher publisher;
    private String suffix;

    @BeforeEach
    void setUp() {
        suffix = RepoTestHelper.suffix();
        publisher = publisherRepo.save(
                RepoTestHelper.publisher(suffix)
        );
    }

    @Test
    void shouldFindByPublisherNameContainingIgnoreCase() {
        assertThat(publisherRepo.findByPublisherNameContainingIgnoreCase(suffix))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());
    }

    @Test
    void shouldFindByPublisherNameIgnoreCase() {
        Optional<Publisher> result =
                publisherRepo.findByPublisherNameIgnoreCase(
                        publisher.getPublisherName().toLowerCase()
                );

        assertThat(result).isPresent();
        assertThat(result.get().getPublisherId())
                .isEqualTo(publisher.getPublisherId());
    }

    @Test
    void shouldFindByCityIgnoreCase() {
        assertThat(publisherRepo.findByCityIgnoreCase(
                publisher.getCity().toLowerCase()
        ))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());
    }

    @Test
    void shouldFindByStateIgnoreCase() {
        assertThat(publisherRepo.findByStateIgnoreCase(
                publisher.getState().toLowerCase()
        ))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());
    }

    @Test
    void shouldFindByCountryIgnoreCase() {
        assertThat(publisherRepo.findByCountryIgnoreCase(
                publisher.getCountry().toLowerCase()
        ))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());
    }

    @Test
    void shouldFindByWebsite() {
        Optional<Publisher> result =
                publisherRepo.findByWebsite(publisher.getWebsite());

        assertThat(result).isPresent();
        assertThat(result.get().getPublisherId())
                .isEqualTo(publisher.getPublisherId());
    }

    @Test
    void shouldCheckIfWebsiteExists() {
        assertThat(publisherRepo.existsByWebsite(publisher.getWebsite()))
                .isTrue();
    }
}