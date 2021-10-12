package com.github.gimaz.testcasevitasoft.repository;

import com.github.gimaz.testcasevitasoft.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
