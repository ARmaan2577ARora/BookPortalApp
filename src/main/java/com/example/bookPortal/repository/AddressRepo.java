package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.AddressProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "addresses", excerptProjection = AddressProjection.class)
public interface AddressRepo extends JpaRepository<Address, Integer> {

    List<AddressProjection> findAddressProjectionByUser_UserId(Integer userId);

    Optional<AddressProjection> findAddressProjectionByUser_UserIdAndIsDefaultTrue(Integer userId);

    List<AddressProjection> findAddressProjectionByCityIgnoreCase(String city);

    List<AddressProjection> findAddressProjectionByUser_EmailIgnoreCase(String email);
    List<Address> findByUser_UserId(Integer userId);

    Optional<Address> findByUser_UserIdAndIsDefaultTrue(Integer userId);

    List<Address> findByCityIgnoreCase(String city);

    List<Address> findByStateIgnoreCase(String state);

    List<Address> findByCountryIgnoreCase(String country);

    List<Address> findByZipcode(String zipcode);

    List<Address> findByUser_UserIdAndCityIgnoreCase(Integer userId, String city);

    List<Address> findByUser_EmailIgnoreCase(String email);
}