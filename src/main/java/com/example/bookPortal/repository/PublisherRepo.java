package com.example.bookPortal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.entity.Publisher;

public interface PublisherRepo extends JpaRepository<Publisher, Integer> {
}
