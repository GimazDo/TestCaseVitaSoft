package com.github.gimaz.testcasevitasoft.repository;

import com.github.gimaz.testcasevitasoft.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
}
