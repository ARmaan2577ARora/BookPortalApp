package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StoreRepo extends JpaRepository<Store, Integer> {
}
