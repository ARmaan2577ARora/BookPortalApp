package com.example.bookPortal.repository;

import com.example.bookPortal.entity.StoreBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreBookRepo extends JpaRepository<StoreBook, Integer> {

}
