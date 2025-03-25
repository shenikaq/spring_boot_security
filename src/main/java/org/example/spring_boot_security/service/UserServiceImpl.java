package org.example.spring_boot_security.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.example.spring_boot_security.model.User;
import org.example.spring_boot_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@NoArgsConstructor
@Primary
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Получение всех пользователей с ролями
    @Override
    public List<User> findAllWithRoles() {
        return userRepository.findAllWithRoles();
    }

    // Поиск пользователя по имени
    @Override
    public Optional<User> findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    // Сохранение пользователя
    @Override
    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    // Обновление пользователя
    @Override
    @Transactional
    public void updateUser(Long id, String userName, String email) {
        userRepository.updateUser(id, userName, email);
    }

    // Удаление пользователя по ID
    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    // Поиск по ID
    @Override
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

}

