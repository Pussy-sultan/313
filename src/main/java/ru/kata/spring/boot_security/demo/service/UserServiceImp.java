package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, @Lazy BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Transactional
    @Override
    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User updateUser) {
        User user = userRepository.findById(updateUser.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String currentPassword = user.getPassword();
        String newPassword = updateUser.getPassword();
        if (!currentPassword.equals(newPassword)) {
            updateUser.setPassword((bCryptPasswordEncoder.encode(updateUser.getPassword())));
        }
        userRepository.save(updateUser);
    }

    @Transactional
    @Override
    public void deleteById(int id) {
        try {
            userRepository.deleteById(id);
        }
        catch (EntityNotFoundException e) {
            throw e;
        }

    }

    @Transactional(readOnly = true)
    @Override
    public User getById(int id) {
        try {
            return userRepository.getById(id);
        }
        catch(EntityNotFoundException e) {
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String getRoleListByUser(User user) {
        return user.getRoles()
                .stream()
                .reduce(
                "", (partialAgeResult, item) -> partialAgeResult + ((partialAgeResult.isEmpty() ? "" : " ") + item.getName()), String::concat
        );
    }

    @Override
    public boolean isAdmin(User user) {
        boolean isAdmin = false;
        for (Role role :user.getRoles()) {
            if (role.getName().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        return isAdmin;
    }

    @Transactional(readOnly = true)
    @Override
    public User getByName(String email) {
        return userRepository.findByName(email);
    }

}
