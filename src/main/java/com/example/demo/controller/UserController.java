package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreateUserDto;
import com.example.demo.dto.GetUserDto;
import com.example.demo.dto.UpdateUserDto;
import com.example.demo.service.UserServiceImpl;

@RestController()
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(path = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GetUserDto createUser(@RequestBody CreateUserDto userDto) {
        return this.userService.createUser(userDto);
    }

    @GetMapping(path = "/{user_id}")
    public GetUserDto getUser(@PathVariable(value = "user_id") int userId) throws Exception {
        return this.userService.getSingleUser(userId);
    }

    @GetMapping(path = "")
    public List<GetUserDto> getAllUser() {
        return this.userService.getAllUser();
    }

    @DeleteMapping(path = "/{user_id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable(value = "user_id") int userId)
            throws Exception {
        this.userService.deleteUser(userId);
        Map<String, String> map = new HashMap<>();
        map.put("message", "User Deleted Successfully");
        return ResponseEntity.ok().body(map);
    }

    @PutMapping(path = "/{user_id}")
    public GetUserDto updateUser(@PathVariable(value = "user_id") int userId, @RequestBody UpdateUserDto userDto)
            throws Exception {
        return this.userService.updateUser(userId, userDto);
    }

}
