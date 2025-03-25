package org.example.spring_boot_security.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.spring_boot_security.model.Role;
import org.example.spring_boot_security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Находим роль по имени
    @Override
    public Optional<Role> findByRole(String name) {
        return roleRepository.findByRole(name);
    }

    @Override
    public Role save (Role role) {
        return roleRepository.save(role);
    }

    // Проверяем существование роли по имени
    @Override
    public boolean existsByRole(String name) {
        return roleRepository.existsByRole(name);
    }

}
