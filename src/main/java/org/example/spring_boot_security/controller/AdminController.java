package org.example.spring_boot_security.controller;

import org.example.spring_boot_security.model.Role;
import org.example.spring_boot_security.model.User;
import org.example.spring_boot_security.service.RoleService;
import org.example.spring_boot_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listUsers(Model model) {
        // Получаем всех пользователей из базы данных
        List<User> users = userService.findAllWithRoles();
        // Добавляем пользователей в модель для передачи в представление
        model.addAttribute("users", users);
        return "admin";
    }

    // для просмотра одного пользователя
    @GetMapping("/users/{id}")
    public String viewUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
////    /delete?id=2

    @GetMapping("/save")
    public String save(@RequestParam String userName, @RequestParam String password, @RequestParam String email) {
        User user = new User(userName, passwordEncoder().encode(password), email);
        // Добавляем роль ROLE_USER по умолчанию
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRole("ROLE_USER").orElseGet(() -> roleService.save(new Role("ROLE_USER"))));
        user.setRole(roles);
        userService.save(user);
        return "redirect:/admin";
    }
////    ?userName=gigi&password=gigi&email=gi@gi

    @GetMapping("/edit")
    public String editUser(@RequestParam Long id, @RequestParam String userName, @RequestParam String email) {
        userService.updateUser(id, userName, email);
        return "redirect:/admin";
    }
////    ?id=2&userName=fn1&email=e1

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
