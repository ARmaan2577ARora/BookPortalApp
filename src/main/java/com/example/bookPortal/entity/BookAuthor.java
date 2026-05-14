package com.example.bookPortal.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "book_authors")
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookAuthorId;

    @Column(name = "author_order")
    private Integer authorOrder;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    public Integer getBookAuthorId() {
        return bookAuthorId;
    }

    public void setBookAuthorId(Integer bookAuthorId) {
        this.bookAuthorId = bookAuthorId;
    }

    public Integer getAuthorOrder() {
        return authorOrder;
    }

    public void setAuthorOrder(Integer authorOrder) {
        this.authorOrder = authorOrder;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
