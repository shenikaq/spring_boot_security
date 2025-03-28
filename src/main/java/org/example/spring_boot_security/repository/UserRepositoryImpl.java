package org.example.spring_boot_security.repository;

import org.example.spring_boot_security.model.Role;
import org.example.spring_boot_security.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toSet;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAllWithRoles() {
        return new ArrayList<>(jdbcTemplate.query("SELECT u.id as user_id, u.user_name, u.password, u.email, r.id as role_id, r.role FROM user u " +
                                            "LEFT JOIN user_role ur ON u.id = ur.user_id " +
                                            "LEFT JOIN role r ON ur.role_id = r.id ",
                                            (rs, rowNum) -> {
                    User user = new User();
                    user.setId(rs.getLong("user_id"));
                    user.setUserName(rs.getString("user_name"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));

                    Role role = new Role();
                    role.setId(rs.getLong("role_id"));
                    role.setRole(rs.getString("role"));

                    Set<Role> roles = new HashSet<>();
                    roles.add(role);
                    user.setRole(roles);
                    return user;
                }).stream()
                .collect(groupingBy(User::getId, collectingAndThen(
                        mapping(u -> u, toSet()),
                        users -> {
                            User result = users.iterator().next();
                            result.setRole(users.stream()
                                    .flatMap(u -> Optional.ofNullable(u.getRole()).orElse(Collections.emptySet()).stream())
                                    .collect(toSet()));
                            return result;
                        }
                ))).values());
    }

    @Override
    public Optional<User> findByUsername(String userName) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT u.id as user_id, u.user_name, u.password, u.email FROM user u WHERE u.user_name = ?",
                                                    new Object[]{userName}, (rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getLong("user_id"));
                u.setUserName(rs.getString("user_name"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                return u;
            });

            Set<Role> roles = jdbcTemplate.query("SELECT r.id as role_id, r.role FROM user_role ur " +
                                                        "LEFT JOIN role r ON ur.role_id = r.id " +
                                                        "WHERE ur.user_id = ?",
                                                        new Object[]{user.getId()}, (rs, rowNum) -> {
                    Role role = new Role();
                    role.setId(rs.getLong("role_id"));
                    role.setRole(rs.getString("role"));
                    return role;
                }).stream().collect(Collectors.toSet());
            user.setRole(roles);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void save(User user) {
//// Использование SqlParameterValue
        SqlParameterValue userName = new SqlParameterValue(Types.VARCHAR, user.getUsername());
        SqlParameterValue password = new SqlParameterValue(Types.VARCHAR, user.getPassword());
        SqlParameterValue email = new SqlParameterValue(Types.VARCHAR, user.getEmail());
        jdbcTemplate.update("INSERT INTO user (user.user_name, user.password, user.email) VALUES (?, ?, ?)",
                                    userName, password, email);

        User getUser = findByUsername(user.getUsername()).get();
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            List<Object[]> roleParams = user.getRole().stream()
                    .map(role -> new Object[]{getUser.getId(), role.getId()})
                    .collect(Collectors.toList());
            jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role_id) VALUES (?, ?)", roleParams);
        }
    }

    @Override
    public void updateUser(Long id, String userName, String email) {
        jdbcTemplate.update("UPDATE user SET user_name = ?, email = ? WHERE id = ?", userName, email, id);
    }

    @Override
    public void deleteUserById(Long id) {
        jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", id);
        jdbcTemplate.update("DELETE FROM user WHERE id = ?", id);
    }

    @Override
    public Optional<User> findById(long userId) {
        try {
            User user = jdbcTemplate.queryForObject("SELECT u.id as user_id, u.user_name, u.password, u.email FROM user u WHERE u.id = ?",
                                                        new Object[]{userId}, (rs, rowNum) -> {
                User u = new User();
                u.setId(rs.getLong("user_id"));
                u.setUserName(rs.getString("user_name"));
                u.setPassword(rs.getString("password"));
                u.setEmail(rs.getString("email"));
                return u;
            });

            Set<Role> roles = jdbcTemplate.query("SELECT r.id as role_id, r.role FROM user_role ur LEFT JOIN role r ON ur.role_id = r.id WHERE ur.user_id = ?",
                                                        new Object[]{user.getId()}, (rs, rowNum) -> {
                Role role = new Role();
                role.setId(rs.getLong("role_id"));
                role.setRole(rs.getString("role"));
                return role;
            }).stream().collect(Collectors.toSet());

            user.setRole(roles);
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
