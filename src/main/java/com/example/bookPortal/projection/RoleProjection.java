package com.example.bookPortal.projection;

import com.example.bookPortal.entity.Roles;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "roleView", types = Roles.class)
public interface RoleProjection {

    Integer getRoleId();

    String getRoleName();
}