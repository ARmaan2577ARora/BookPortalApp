package com.example.bookPortal.projection;

import com.example.bookPortal.entity.BookAuthor;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "bookAuthorView", types = BookAuthor.class)
public interface BookAuthorProjection {

    BookAuthorIdInfo getId();

    Integer getAuthorOrder();

    BookInfo getBook();

    AuthorInfo getAuthor();

    interface BookAuthorIdInfo {

        Integer getBookId();

        Integer getAuthorId();
    }

    interface BookInfo {

        Integer getBookId();

        String getTitle();

        String getIsbn();

        String getLanguage();

        Integer getPages();
    }

    interface AuthorInfo {

        Integer getAuthorId();

        String getFirstName();

        String getLastName();

        String getCountry();
    }
}