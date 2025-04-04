package com.bookstore;

import com.bookstore.dto.*;
import com.bookstore.enums.Role;
import com.bookstore.service.BookService;
import com.bookstore.service.BookTypeService;
import com.bookstore.service.CustomerService;
import com.bookstore.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.math.BigDecimal;
import java.util.Locale;

@Configuration
public class AppConfig {

    private final BookTypeService bookTypeService;
    private final BookService bookService;
    private final CustomerService customerService;
    private final UserService userService;

    public AppConfig(BookTypeService bookTypeService, BookService bookService, CustomerService customerService, UserService userService) {
        this.bookTypeService = bookTypeService;
        this.bookService = bookService;
        this.customerService = customerService;
        this.userService = userService;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.UK);
        return slr;
    }

    @PostConstruct
    public void dataSetup(){
        if(bookTypeService.findAll().isEmpty()){

            BookTypeResponseDTO newReleasesResponse = bookTypeService.create(
                    new BookTypeRequestDTO("New Releases", new BigDecimal("0"),null, new BigDecimal("0"),false)
            );
            BookTypeResponseDTO regularResponse = bookTypeService.create(
                    new BookTypeRequestDTO("Regular", new BigDecimal(0),3,new BigDecimal("0.1"),true)
            );
            BookTypeResponseDTO oldEditionsResponse = bookTypeService.create(
                    new BookTypeRequestDTO("Old editions", new BigDecimal("0.2"), 3, new BigDecimal("0.05"),true)
            );

            bookService.create(
                    new BookRequestDTO("Clean Code: A Handbook of Agile Software Craftsmanship", new BigDecimal("40.90"),50L,null,null,null, regularResponse)
            );

            bookService.create(
                    new BookRequestDTO("Harry Potter and the Philosopher's Stone", new BigDecimal("50.90"),100L,null,null,null, newReleasesResponse)
            );
            bookService.create(
                    new BookRequestDTO("Sherlock Holmes: A study in scarlet", new BigDecimal("29.90"),70L,null,null,null, oldEditionsResponse)
            );

            UserResponseDTO user = userService.create(new UserRequestDTO("admin", "1234", Role.ADMIN));

            customerService.create(
                    new CustomerRequestDTO("Edson Cruz", "edson.l.cruz@gmail.com", user)
            );
        }
    }
}
