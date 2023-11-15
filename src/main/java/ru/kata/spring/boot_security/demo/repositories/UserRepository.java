package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

@Repository
public interface UserRepository {
    List<User> findAll();

    User getById(Long id);

    void save(User user);

    void updateUser(User user);

    void deleteById(Long id);

    User findByUsername(String username);

}
