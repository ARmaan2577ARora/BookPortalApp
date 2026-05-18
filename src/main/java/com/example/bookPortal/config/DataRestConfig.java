package com.example.bookPortal.config;

import com.example.bookPortal.entity.*;
import com.example.bookPortal.projection.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {
        config.exposeIdsFor(
                Roles.class,
                User.class,
                Address.class,
                Author.class,
                Publisher.class,
                Book.class,
                BookAuthor.class,
                Store.class,
                StoreBook.class,
                Order.class,
                OrderItem.class
        );

        config.getProjectionConfiguration().addProjection(RoleProjection.class);
        config.getProjectionConfiguration().addProjection(UserProjection.class);
        config.getProjectionConfiguration().addProjection(AddressProjection.class);
        config.getProjectionConfiguration().addProjection(AuthorProjection.class);
        config.getProjectionConfiguration().addProjection(PublisherProjection.class);
        config.getProjectionConfiguration().addProjection(BookProjection.class);
        config.getProjectionConfiguration().addProjection(BookAuthorProjection.class);
        config.getProjectionConfiguration().addProjection(StoreProjection.class);
        config.getProjectionConfiguration().addProjection(StoreBookProjection.class);
        config.getProjectionConfiguration().addProjection(OrderProjection.class);
        config.getProjectionConfiguration().addProjection(OrderItemProjection.class);
    }
}