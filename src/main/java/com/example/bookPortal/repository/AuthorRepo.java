package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.AuthorProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "authors", excerptProjection = AuthorProjection.class)
public interface AuthorRepo extends JpaRepository<Author, Integer> {

    List<AuthorProjection> findAuthorProjectionByFirstNameContainingIgnoreCase(String firstName);

    List<AuthorProjection> findAuthorProjectionByLastNameContainingIgnoreCase(String lastName);

    List<AuthorProjection> findAuthorProjectionByCountryIgnoreCase(String country);

    Optional<AuthorProjection> findAuthorProjectionByPhone(String phone);

    List<AuthorProjection> findAuthorProjectionByBioContainingIgnoreCase(String keyword);
    List<Author> findByFirstNameContainingIgnoreCase(String firstName);

    List<Author> findByLastNameContainingIgnoreCase(String lastName);

    List<Author> findByCountryIgnoreCase(String country);

    Optional<Author> findByPhone(String phone);

    boolean existsByPhone(String phone);

    Optional<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

    List<Author> findByFirstNameStartingWithIgnoreCase(String prefix);

    List<Author> findByLastNameStartingWithIgnoreCase(String prefix);

    List<Author> findByBioContainingIgnoreCase(String keyword);
}