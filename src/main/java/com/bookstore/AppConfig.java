package com.bookstore;

import com.bookstore.entity.Book;
import com.bookstore.entity.BookType;
import com.bookstore.entity.Customer;
import com.bookstore.entity.User;
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
            BookType newReleases = new BookType();
            newReleases.setName("New Releases");
            newReleases.setDiscountOverall(new BigDecimal("0"));
            newReleases.setMinimalBundle(null);
            newReleases.setDiscountBundle(new BigDecimal("0"));
            newReleases.setPayableWithPoints(false);

            BookType regular = new BookType();
            regular.setName("Regular");
            regular.setDiscountOverall(new BigDecimal(0));
            regular.setMinimalBundle(3);
            regular.setDiscountBundle(new BigDecimal("0.1"));
            regular.setPayableWithPoints(true);

            BookType oldEditions = new BookType();
            oldEditions.setName("Old editions");
            oldEditions.setDiscountOverall(new BigDecimal("0.2"));
            oldEditions.setMinimalBundle(3);
            oldEditions.setDiscountBundle(new BigDecimal("0.05"));
            oldEditions.setPayableWithPoints(true);

            newReleases = bookTypeService.create(newReleases);
            regular = bookTypeService.create(regular);
            oldEditions = bookTypeService.create(oldEditions);

            Book book1 = new Book();
            book1.setTitle("Clean Code: A Handbook of Agile Software Craftsmanship");
            book1.setPrice(new BigDecimal("40.90"));
            book1.setAmount(50L);
            book1.setBookType(regular);

            Book book2 = new Book();
            book2.setTitle("Harry Potter and the Philosopher's Stone");
            book2.setPrice(new BigDecimal("50.90"));
            book2.setAmount(100L);
            book2.setBookType(newReleases);

            Book book3 = new Book();
            book3.setTitle("Sherlock Holmes: A study in scarlet");
            book3.setPrice(new BigDecimal("29.90"));
            book3.setAmount(70L);
            book3.setBookType(oldEditions);

            book1 = bookService.create(book1);
            book2 = bookService.create(book2);
            book3 = bookService.create(book3);

            User user = new User();
            user.setUsername("admin");
            user.setPassword("1234");
            user.setRole(Role.ADMIN);

            userService.create(user);

            Customer customer1 = new Customer();
            customer1.setName("Edson Cruz");
            customer1.setEmail("edson.l.cruz@gmail.com");
            customer1.setUser(user);

            customer1 = customerService.create(customer1);
        }
    }
}
