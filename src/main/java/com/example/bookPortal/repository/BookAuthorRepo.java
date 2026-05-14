package com.example.bookPortal.repository;

import com.example.bookPortal.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepo extends JpaRepository<BookAuthor,Integer> {
}
