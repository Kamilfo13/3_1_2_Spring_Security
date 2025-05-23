package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {

        this.userService=userService;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping("/create")
    public String createUserForm(User user, Model model) {
        model.addAttribute("roleList", userService.listRoles());
        return "create";
    }

    @PostMapping("/create")
    public String createUser (User newUser ) {
        List<String> roleNames = newUser.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toList());
        List<Role> assignedRoles = userService.listByRole(roleNames);

        if (assignedRoles.isEmpty()) {
            return "redirect:/admin?error=rolesNotFound";
        }

        newUser.setRoles(assignedRoles);
        userService.add(newUser);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roleList", userService.listRoles());
        return "update";
    }

    @PostMapping("/update")
    public String updateUser(User user) {
        List<String> lsr = user.getRoles().stream().map(r->r.getRole()).collect(Collectors.toList());
        List<Role> liRo = userService.listByRole(lsr);
        user.setRoles(liRo);
        userService.update(user);
        return "redirect:/admin";
    }
}
