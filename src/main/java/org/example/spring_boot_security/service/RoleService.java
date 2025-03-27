package org.example.spring_boot_security.service;

import org.example.spring_boot_security.model.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> findByRole(String name);
    Role save(Role role);

}
