package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Integer> {

    List<Address> findByUser_UserId(Integer userId);

    Optional<Address> findByUser_UserIdAndIsDefaultTrue(Integer userId);

    List<Address> findByCityIgnoreCase(String city);

    List<Address> findByStateIgnoreCase(String state);

    List<Address> findByCountryIgnoreCase(String country);

    List<Address> findByZipcode(String zipcode);

    List<Address> findByUser_UserIdAndCityIgnoreCase(Integer userId, String city);

    List<Address> findByUser_EmailIgnoreCase(String email);
}