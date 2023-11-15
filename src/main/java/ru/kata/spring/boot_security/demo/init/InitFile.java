package ru.kata.spring.boot_security.demo.init;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServices;
import ru.kata.spring.boot_security.demo.service.UserServices;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;


@Component
public class InitFile {

    UserServices    userService;
    RoleServices roleService;

    @Autowired
    public InitFile(UserServices userService, RoleServices roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }
    @Transactional
    @PostConstruct
    public void run() {

        roleService.addRole(new Role("ROLE_ADMIN"));
        roleService.addRole(new Role("ROLE_USER"));

        Set<Role> adminRole = new HashSet<>();
        Set<Role> userRole = new HashSet<>();
        adminRole.add(roleService.getRoleById(2l));
        adminRole.add(roleService.getRoleById(1L));
        userRole.add(roleService.getRoleById(2L));

        userService.updateUser(new User("Igor", "Ostrovsky", (byte) 52, "Russian", "admin", "$2a$12$KPdGDctM2qJTep1jLsxSGu6JT7pTvYcGz6bt1mhJjtj5cZ3yy/th2", adminRole));
        userService.updateUser(new User("Peter", "Petrov", (byte) 25, "Russian", "user", "$2a$12$H.z3up/SlBAaEDL2DQVgOOo85hoaWknYd.ER6c8B7Hmbl5fQocKJm", userRole));
    }
}
