package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepo extends JpaRepository<Author, Integer> {

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