package org.example.spring_boot_security.repository;

import org.example.spring_boot_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

// Получение всех пользователей с ролями
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.role")
    List<User> findAllWithRoles();

// Поиск пользователя по имени
    @Query("Select u from User u left join fetch u.role where u.userName=:userName")
    Optional<User> findByUsername(@Param("userName") String userName);

// Сохранение пользователя
    @Override
    @Modifying
    User save(User user);

// Обновление пользователя
    @Modifying
    @Query("UPDATE User u SET u.userName = :user_name, u.email = :email WHERE u.id = :id")
//    void updateUser(Long id, String name, String email);
    void updateUser(@Param("id") Long id,
                    @Param("user_name") String name,
                    @Param("email") String email);

// Удаление пользователя по ID
    @Modifying
    void deleteUserById(Long id);

// Поиск по ID
    Optional<User> findById(Long id);

}
