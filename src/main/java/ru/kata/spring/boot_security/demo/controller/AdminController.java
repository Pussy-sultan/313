package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.*;

@RequestMapping(value = "/admin")
@Controller
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User findedUser = userService.getByName(userDetails.getUsername());
        List<User> userList = userService.getAll();
        Map<Integer, String> mapa = new HashMap<>();
        for (User user : userList) {
            mapa.put(user.getId(), userService.getRoleListByUser(user));
        }

        model.addAttribute("currentUser", findedUser);
        model.addAttribute("rolesThisUser", mapa.get(findedUser.getId()));
        model.addAttribute("userObject", new User());
        model.addAttribute("users", userList);
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("roles", mapa);
        return "users";
    }

    @DeleteMapping(value = "/delete/")
    public String deleteUser(@RequestParam(value = "id") int id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser (@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping()
    public String saveUser(@ModelAttribute("newUser") User user) {

        userService.save(user);
        return "redirect:/admin";
    }
}
