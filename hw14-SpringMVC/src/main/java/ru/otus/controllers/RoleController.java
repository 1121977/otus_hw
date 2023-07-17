package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.core.model.Client;
import ru.otus.core.model.Role;
import ru.otus.core.dao.RoleDao;
import java.util.List;
import java.util.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.*;

@Controller
public class RoleController {
    private final RoleDao roleDao;

    public RoleController(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @GetMapping({"/role/list"})
    public String rolesListView(Model model) {
        List<Role> roles = roleDao.findAll();
        model.addAttribute("roles", roles);
        return "rolesList.html";
    }

    @GetMapping("/role/create")
    public String roleCreateView(Model model) {
        model.addAttribute("role", new Role());
        return "roleCreate.html";
    }

    @PostMapping("/role/save")
    public RedirectView roleSave(@ModelAttribute Role role, @RequestParam HashMap<String, String> reqMap) {
        roleDao.insertOrUpdate(role);
        return new RedirectView("/role/list", true);
    }

    @GetMapping("/role/{roleID}")
    public String roleEditView(Model model, @PathVariable Long roleID) {
        Role role = null;
        try {
            role = roleDao.findById(roleID).get();
            model.addAttribute("role", role);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "roleCreate.html";
    }

    @PostMapping("/role/delete")
    public RedirectView roleDelete(@ModelAttribute Role role) {
        roleDao.delete(role);
        return new RedirectView("/role/list", true);
    }
}

