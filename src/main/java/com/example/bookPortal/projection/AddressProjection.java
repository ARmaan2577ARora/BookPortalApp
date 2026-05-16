package com.example.bookPortal.projection;

import com.example.bookPortal.entity.Address;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "addressView", types = Address.class)
public interface AddressProjection {

    Integer getAddressId();

    String getHouseAddress();

    String getCity();

    String getState();

    String getZipcode();

    String getCountry();

    Boolean getIsDefault();

    UserInfo getUser();

    interface UserInfo {

        Integer getUserId();

        String getFullName();

        String getEmail();

        String getPhone();
    }
}