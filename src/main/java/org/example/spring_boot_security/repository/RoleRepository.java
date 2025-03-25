package org.example.spring_boot_security.repository;

import org.example.spring_boot_security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Находим роль по имени
    Optional<Role> findByRole(String name);

    Role save(Role role);

    // Проверяем существование роли по имени
    boolean existsByRole(String name);
}
