package org.example.spring_boot_security.controller;

import org.example.spring_boot_security.model.Role;
import org.example.spring_boot_security.model.User;
import org.example.spring_boot_security.model.UserRegistrationRequest;
import org.example.spring_boot_security.service.RoleService;
import org.example.spring_boot_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.Set;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new UserRegistrationRequest());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {

        if (userService.findByUsername(user.getUsername()).isEmpty()) {
            // Добавляем роль ROLE_USER по умолчанию
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.findByRole("ROLE_USER").orElseGet(() -> roleService.save(new Role("ROLE_USER"))));
            user.setRole(roles);
            // Хеширование пароля
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // Сохраняем пользователя
            userService.save(user);
            return "redirect:/login?registered=true";
        } else {
            return "register";
        }
    }

}
