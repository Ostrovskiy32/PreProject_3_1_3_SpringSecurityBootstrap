package ru.kata.spring.boot_security.demo.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServices;
import ru.kata.spring.boot_security.demo.service.UserServices;


import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Set;


@Controller
@Valid
@RequestMapping("/admin")
public class AdminController {

    private final RoleServices roleServices;
    private final UserServices userServices;
    private static final String REDIRECT = "redirect:/admin";

    @Autowired
    public AdminController(RoleServices roleServices, UserServices userServices) {
        this.roleServices = roleServices;
        this.userServices = userServices;
    }

    private void extracted(Model model) {
        model.addAttribute("allRoles", roleServices.getAllRoles());
    }

    @GetMapping(value = "/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userServices.getAllUsers());
        return "users";
    }

    @GetMapping(value = "")
    public String getAdminPanel(Principal principal, Model model) {
        model.addAttribute("user", userServices.getUserByUsername(principal.getName()));
        return "users_form";
    }

//    @GetMapping(value = "/{id}")
//    public String getUserById(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("user", userServices.getUserById(id));
//        return "user";
//    }

    @GetMapping(value = "/new")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        extracted(model);
        return "create";
    }

    @PostMapping(value = "/new")
    public String add(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam List<Long> ids) {

        // Checking validation exception
        if (bindingResult.hasErrors()) {
            System.out.println(user);
            if (userServices.isUsernameNotUnique(user.getUsername())) {
                bindingResult.rejectValue("username", "error.username", "Name has to be unique");
            }
            return "create";
        } else {
            Set<Role> assignedRole = roleServices.findAllRoleId(ids);
            user.setRoles(assignedRole);
            userServices.addUser(user);
            return REDIRECT;
        }
    }

    @GetMapping(value = "/form/{id}")
    public String deleteUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServices.getUserById(id));
        extracted(model);
        return "delete_form";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        userServices.removeUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServices.getUserById(id));
        extracted(model);
        return "edit";
    }

    @PatchMapping(value = "/save")
    public ResponseEntity<?> update(@Valid @ModelAttribute("user") User user, BindingResult bindingResult,
                         @RequestParam List<Long> ids) {

        // Checking validation exception
        if (bindingResult.hasErrors()) {
            System.out.println(user);
            if (userServices.isUsernameNotUnique(user.getUsername())) {
                bindingResult.rejectValue("username", "error.username", "Name has to be unique");
            }
            return ResponseEntity.ok(user);
        } else {
            Set<Role> assignedRole = roleServices.findAllRoleId(ids);
            user.setRoles(assignedRole);
            userServices.updateUser(user);
            return ResponseEntity.ok().build();
        }
    }
}
