package org.example.spring_boot_security.service;

import org.example.spring_boot_security.model.Role;
import org.example.spring_boot_security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        //// Создаем роли
        Role role1 = new Role("ROLE_USER");
        Role role2 = new Role("ROLE_ADMIN");
        //// Сохраняем роли в БД
        role1 = roleService.save(role1);
        role2 = roleService.save(role2);

        Set<Role> roleUser = new HashSet<>();
        roleUser.add(role1);

        Set<Role> roleAdmin = new HashSet<>();
        roleAdmin.add(role2);
        roleAdmin.add(role1);

        //// Создаем пользователя
        User user1 = new User("dasha", passwordEncoder.encode("dasha"), "dasha@dd.ru");
        //// Назначаем роли пользователю
        user1.setRole(roleAdmin);
        //// Сохраняем пользователя
        userService.save(user1);

        User user2 = new User("Alice", passwordEncoder.encode("aa"), "alice@aa.ru");
        user2.setRole(roleUser);
        userService.save(user2);

        User user3 = new User("Sara", passwordEncoder.encode("ss"), "sara@ss.com");
        user3.setRole(roleUser);
        userService.save(user3);

        User user4 = new User("Flora", passwordEncoder.encode("ff"), "flora@ff.com");
        user4.setRole(roleUser);
        userService.save(user4);

    }

}
