package org.example.spring_boot_security.repository;

import org.example.spring_boot_security.model.Role;

import java.util.Optional;

public interface RoleRepository {

    // Находим роль по имени
    Optional<Role> findByRole(String name);

    Role save(Role role);

}
