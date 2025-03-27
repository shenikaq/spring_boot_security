package org.example.spring_boot_security.repository;

import org.example.spring_boot_security.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Role> findByRole(String userRole) {
        String sqlRole = "SELECT " +
                "r.id as role_id," +
                "r.role " +
                "FROM role r " +
                "WHERE r.role = ?";

        try {
            Role roles = jdbcTemplate.queryForObject(sqlRole, new Object[]{userRole}, (rs, rowNum) -> {
                Role role = new Role();
                role.setId(rs.getLong("role_id"));
                role.setRole(rs.getString("role"));
                return role;
            });

            return Optional.of(roles);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Role save(Role role) {
        String sql = "INSERT INTO role (role.role)"
                + "VALUES (?)";
//// Использование SqlParameterValue
        SqlParameterValue roles = new SqlParameterValue(Types.VARCHAR, role.getRole());
        jdbcTemplate.update(sql, roles);
        Role getRole = findByRole(role.getRole()).get();
        return getRole;
    }

}
