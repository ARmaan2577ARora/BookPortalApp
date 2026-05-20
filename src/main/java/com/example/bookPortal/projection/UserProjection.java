package com.example.bookPortal.projection;

import com.example.bookPortal.entity.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "userView", types = User.class)
public interface UserProjection {

    Integer getUserId();

    String getFullName();

    String getEmail();

    String getPhone();

    Boolean getIsActive();

    RoleProjection getRole();
}