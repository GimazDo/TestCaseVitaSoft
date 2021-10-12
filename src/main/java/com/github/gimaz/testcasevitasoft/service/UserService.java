package com.github.gimaz.testcasevitasoft.service;

import com.github.gimaz.testcasevitasoft.entity.Role;
import com.github.gimaz.testcasevitasoft.entity.User;

import java.util.List;

public interface UserService {

    boolean add(User user);

    boolean update(User user);


    boolean addRoleToUser(String username, String roleName) throws Exception;

    User findByUsername(String username);

    List<User> getAll();

}
