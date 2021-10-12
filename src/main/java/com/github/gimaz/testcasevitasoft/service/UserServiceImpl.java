package com.github.gimaz.testcasevitasoft.service;

import com.github.gimaz.testcasevitasoft.entity.Role;
import com.github.gimaz.testcasevitasoft.entity.User;
import com.github.gimaz.testcasevitasoft.repository.RoleRepository;
import com.github.gimaz.testcasevitasoft.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User with username {} not found in database", username);
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User with username {} found in database", username);

        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public boolean add(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            log.warn("IN add - User with username {} is already exists", user.getUsername());
            return false;
        }
        userRepository.save(user);
        log.info("IN add - User with username {} successfully added", user.getUsername());
        return true;
    }

    @Override
    public boolean update(User user) {
        userRepository.save(user);
        log.info("IN update - User with username {} updated", user.getUsername());
        return true;
    }


    @Override
    public boolean addRoleToUser(String username, String roleName) throws Exception {
        Role role = roleRepository.findByName(roleName);
        User user = userRepository.findByUsername(username);
        if (role == null) {
            log.error("IN addRoleToUser - Role {} doesn't exist", roleName);
            throw new Exception("Role with name " + roleName + " not found");
        }
        if (user == null) {
            log.error("IN addRoleToUser - User {} doesn't exist", roleName);
            throw new Exception("User with username " + username + " not found");
        }
        if (user.getRoles().contains(role)) {
            log.error("IN addRoleToUser - User {} already has Role {}", username, roleName);
            throw new Exception("User with username " + username + " already has Role " + roleName);
        }
        user.getRoles().add(role);
        update(user);
        return true;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepository.findAll();
        log.info("IN getAll - Return all({}) users from DB", users.size());
        return users;
    }
}
