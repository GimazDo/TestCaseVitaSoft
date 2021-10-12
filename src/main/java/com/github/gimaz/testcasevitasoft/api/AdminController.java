package com.github.gimaz.testcasevitasoft.api;

import com.github.gimaz.testcasevitasoft.entity.User;
import com.github.gimaz.testcasevitasoft.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/admin/")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping("getAllUsers")
    public @ResponseBody
    List<User> getAllUsers() {
        List<User> users = userService.getAll();
        for (User user : users) {
            user.setRequests(null);
        }
        return users;
    }

    @PostMapping("addRoleToUser")
    public @ResponseBody
    HashMap<String, Object> doOperator(@RequestBody User user) {

        HashMap<String, Object> response = new HashMap<>();
        String status = "";
        String error = "";
        if (user.getUsername() == null) {
            status = "error";
            error = "Username cannot be null";
            response.put("status", status);
            response.put("error", error);
        } else {
            try {
                userService.addRoleToUser(user.getUsername(), "ROLE_OPERATOR");
                status = "success";
                response.put("status", status);
            } catch (Exception e) {
                status = "error";
                error = e.getMessage();
                response.put("status", status);
                response.put("error", error);
            }
        }
        return response;
    }
}
