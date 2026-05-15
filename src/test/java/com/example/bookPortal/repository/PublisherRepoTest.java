package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Publisher;
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
class PublisherRepoTest {

    @Autowired
    private PublisherRepo publisherRepo;

    @Test
    void testPublisherRepoMethods() {
        String suffix = RepoTestHelper.suffix();

        Publisher publisher = publisherRepo.save(RepoTestHelper.publisher(suffix));

        assertThat(publisherRepo.findByPublisherNameContainingIgnoreCase(suffix))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());

        Optional<Publisher> byName = publisherRepo.findByPublisherNameIgnoreCase(
                publisher.getPublisherName().toLowerCase()
        );
        assertThat(byName).isPresent();

        assertThat(publisherRepo.findByCityIgnoreCase(publisher.getCity().toLowerCase()))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());

        assertThat(publisherRepo.findByStateIgnoreCase(publisher.getState().toLowerCase()))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());

        assertThat(publisherRepo.findByCountryIgnoreCase(publisher.getCountry().toLowerCase()))
                .extracting(Publisher::getPublisherId)
                .contains(publisher.getPublisherId());

        Optional<Publisher> byWebsite = publisherRepo.findByWebsite(publisher.getWebsite());
        assertThat(byWebsite).isPresent();

        assertThat(publisherRepo.existsByWebsite(publisher.getWebsite())).isTrue();
    }
}