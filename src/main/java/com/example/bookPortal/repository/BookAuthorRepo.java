package com.example.bookPortal.repository;

import com.example.bookPortal.entity.BookAuthor;
import com.example.bookPortal.entity.BookAuthorId;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.BookAuthorProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "bookAuthors", excerptProjection = BookAuthorProjection.class)
public interface BookAuthorRepo extends JpaRepository<BookAuthor, BookAuthorId> {

    List<BookAuthorProjection> findBookAuthorProjectionByBook_BookId(Integer bookId);

    List<BookAuthorProjection> findBookAuthorProjectionByAuthor_AuthorId(Integer authorId);

    List<BookAuthorProjection> findBookAuthorProjectionByBook_BookIdOrderByAuthorOrderAsc(Integer bookId);

    List<BookAuthorProjection> findBookAuthorProjectionByAuthor_FirstNameContainingIgnoreCase(String firstName);

    List<BookAuthorProjection> findBookAuthorProjectionByAuthor_LastNameContainingIgnoreCase(String lastName);

    List<BookAuthorProjection> findBookAuthorProjectionByBook_TitleContainingIgnoreCase(String title);
    List<BookAuthor> findByBook_BookId(Integer bookId);

    List<BookAuthor> findByAuthor_AuthorId(Integer authorId);

    List<BookAuthor> findByBook_BookIdOrderByAuthorOrderAsc(Integer bookId);

    Optional<BookAuthor> findByBook_BookIdAndAuthor_AuthorId(Integer bookId, Integer authorId);

    boolean existsByBook_BookIdAndAuthor_AuthorId(Integer bookId, Integer authorId);

    List<BookAuthor> findByAuthorOrder(Integer authorOrder);

    List<BookAuthor> findByAuthor_FirstNameContainingIgnoreCase(String firstName);

    List<BookAuthor> findByAuthor_LastNameContainingIgnoreCase(String lastName);

    List<BookAuthor> findByBook_TitleContainingIgnoreCase(String title);
}