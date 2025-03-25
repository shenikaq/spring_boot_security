package org.example.spring_boot_security.configs;

import org.example.spring_boot_security.model.Role;
import org.example.spring_boot_security.model.User;
import org.example.spring_boot_security.service.RoleService;
import org.example.spring_boot_security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final SuccessUserHandler successUserHandler;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/index").permitAll()
                .requestMatchers("/user").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/register").permitAll()
                .requestMatchers("/process-register").permitAll()
                .requestMatchers("/admin/*", "/admin").hasRole("ADMIN")
                .anyRequest().permitAll());
        httpSecurity.formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/process-login")
                        .successHandler(successUserHandler)
//                        .defaultSuccessUrl("/user", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                );
        httpSecurity.logout(lOut -> lOut.invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                    .logoutSuccessUrl("/login?logout")
                    .permitAll());
        return  httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        User user = new User("dasha", passwordEncoder().encode("dasha"), "dasha@d");
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByRole("ROLE_USER").orElseGet(() -> roleService.save(new Role("ROLE_USER"))));
        roles.add(roleService.findByRole("ROLE_ADMIN").orElseGet(() -> roleService.save(new Role("ROLE_ADMIN"))));
        user.setRole(roles);
        // Сохраняем пользователя
        userService.save(user);
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
