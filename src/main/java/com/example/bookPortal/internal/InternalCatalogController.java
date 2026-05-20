package com.example.bookPortal.internal;

import com.example.bookPortal.entity.*;
import com.example.bookPortal.repository.*;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/internal/catalog")
public class InternalCatalogController {
    private final BookRepo bookRepo;
    private final BookAuthorRepo bookAuthorRepo;
    private final StoreBookRepo storeBookRepo;
    private final AuthorRepo authorRepo;
    private final PublisherRepo publisherRepo;
    private final StoreRepo storeRepo;

    public InternalCatalogController(BookRepo bookRepo, BookAuthorRepo bookAuthorRepo, StoreBookRepo storeBookRepo,
                                     AuthorRepo authorRepo, PublisherRepo publisherRepo, StoreRepo storeRepo) {
        this.bookRepo = bookRepo;
        this.bookAuthorRepo = bookAuthorRepo;
        this.storeBookRepo = storeBookRepo;
        this.authorRepo = authorRepo;
        this.publisherRepo = publisherRepo;
        this.storeRepo = storeRepo;
    }

    @GetMapping("/books")
    public Map<String, Object> books(@RequestParam(required = false) String q,
                                     @RequestParam(required = false) Integer authorId,
                                     @RequestParam(required = false) Integer publisherId) {
        List<Book> books;
        if (authorId != null) {
            books = bookAuthorRepo.findByAuthor_AuthorId(authorId).stream().map(BookAuthor::getBook).filter(Objects::nonNull).distinct().toList();
        } else if (publisherId != null) {
            books = bookRepo.findByPublisher_PublisherId(publisherId);
        } else if (q != null && !q.isBlank()) {
            String query = q.trim();
            Map<Integer, Book> result = new LinkedHashMap<>();
            bookRepo.findByTitleContainingIgnoreCase(query).forEach(book -> result.put(book.getBookId(), book));
            bookRepo.findByPublisher_PublisherNameContainingIgnoreCase(query).forEach(book -> result.put(book.getBookId(), book));
            bookAuthorRepo.findByAuthor_FirstNameContainingIgnoreCase(query).forEach(ba -> { if (ba.getBook() != null) result.put(ba.getBook().getBookId(), ba.getBook()); });
            bookAuthorRepo.findByAuthor_LastNameContainingIgnoreCase(query).forEach(ba -> { if (ba.getBook() != null) result.put(ba.getBook().getBookId(), ba.getBook()); });
            books = new ArrayList<>(result.values());
        } else {
            books = bookRepo.findAll(Sort.by("title"));
        }
        return Map.of("books", books.stream().map(this::bookMap).toList());
    }

    @GetMapping("/books/{bookId}")
    public Map<String, Object> book(@PathVariable Integer bookId) {
        Book book = bookRepo.findById(bookId).orElseThrow();
        return Map.of("book", bookMap(book), "storeBooks", sortedStoreBooks(bookId).stream().map(BackendMapper::storeBook).toList());
    }

    @GetMapping("/books/{bookId}/stores")
    public Map<String, Object> bookStores(@PathVariable Integer bookId, @RequestParam(required = false) String city) {
        Book book = bookRepo.findById(bookId).orElseThrow();
        List<StoreBook> storeBooks = sortedStoreBooks(bookId);
        if (city != null && !city.isBlank()) {
            String c = city.trim();
            storeBooks = storeBooks.stream().filter(sb -> sb.getStore() != null && c.equalsIgnoreCase(sb.getStore().getCity())).toList();
        }
        return Map.of("book", bookMap(book), "storeBooks", storeBooks.stream().map(BackendMapper::storeBook).toList(), "city", city == null ? "" : city);
    }

    @GetMapping("/store-books/{storeBookId}")
    public Map<String, Object> storeBook(@PathVariable Integer storeBookId) {
        StoreBook storeBook = storeBookRepo.findById(storeBookId).orElseThrow();
        return Map.of("storeBook", BackendMapper.storeBook(storeBook));
    }

    @GetMapping("/authors-publishers")
    public Map<String, Object> authorsPublishers(@RequestParam(required = false) String q) {
        List<Author> authors = (q != null && !q.isBlank()) ? authorRepo.findByFirstNameContainingIgnoreCase(q.trim()) : authorRepo.findAll(Sort.by("firstName"));
        if (q != null && !q.isBlank()) {
            LinkedHashMap<Integer, Author> map = new LinkedHashMap<>();
            authors.forEach(a -> map.put(a.getAuthorId(), a));
            authorRepo.findByLastNameContainingIgnoreCase(q.trim()).forEach(a -> map.put(a.getAuthorId(), a));
            authors = new ArrayList<>(map.values());
        }
        List<Publisher> publishers = (q != null && !q.isBlank()) ? publisherRepo.findByPublisherNameContainingIgnoreCase(q.trim()) : publisherRepo.findAll(Sort.by("publisherName"));
        return Map.of("authors", authors.stream().map(BackendMapper::author).toList(),
                      "publishers", publishers.stream().map(BackendMapper::publisher).toList());
    }

    @GetMapping("/stores")
    public Map<String, Object> stores(@RequestParam(required = false) String city) {
        List<Store> stores = (city != null && !city.isBlank()) ? storeRepo.findByCityIgnoreCase(city.trim()) : storeRepo.findAll(Sort.by("storeName"));
        Map<Integer, Long> counts = new LinkedHashMap<>();
        stores.forEach(store -> counts.put(store.getStoreId(), (long) storeBookRepo.findByStore_StoreId(store.getStoreId()).size()));
        return Map.of("stores", stores.stream().map(BackendMapper::store).toList(), "storeCounts", counts);
    }

    @GetMapping("/stores/{storeId}/books")
    public Map<String, Object> storeBooks(@PathVariable Integer storeId) {
        Store store = storeRepo.findById(storeId).orElseThrow();
        List<StoreBook> books = storeBookRepo.findByStore_StoreId(storeId).stream().sorted(Comparator.comparing(sb -> sb.getBook() == null ? "" : sb.getBook().getTitle(), String.CASE_INSENSITIVE_ORDER)).toList();
        return Map.of("store", BackendMapper.store(store), "storeBooks", books.stream().map(BackendMapper::storeBook).toList());
    }

    private Map<String, Object> bookMap(Book book) {
        List<Author> authors = bookAuthorRepo.findByBook_BookIdOrderByAuthorOrderAsc(book.getBookId()).stream().map(BookAuthor::getAuthor).filter(Objects::nonNull).toList();
        List<StoreBook> storeBooks = storeBookRepo.findByBook_BookId(book.getBookId());
        BigDecimal minPrice = storeBooks.stream().map(StoreBook::getPrice).filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        Integer totalStock = storeBooks.stream().map(StoreBook::getStockQuantity).filter(Objects::nonNull).reduce(0, Integer::sum);
        return BackendMapper.book(book, authors, minPrice, totalStock);
    }

    private List<StoreBook> sortedStoreBooks(Integer bookId) {
        return storeBookRepo.findByBook_BookId(bookId).stream().sorted(Comparator.comparing(StoreBook::getPrice, Comparator.nullsLast(BigDecimal::compareTo))).toList();
    }
}
