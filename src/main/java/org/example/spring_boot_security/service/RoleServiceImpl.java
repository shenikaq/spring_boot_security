package org.example.spring_boot_security.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.spring_boot_security.model.Role;
import org.example.spring_boot_security.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Находим роль по имени
    @Override
    public Optional<Role> findByRole(String name) {
        return roleRepository.findByRole(name);
    }

    @Override
    @Transactional
    public Role save (Role role) {
        return roleRepository.save(role);
    }

}
