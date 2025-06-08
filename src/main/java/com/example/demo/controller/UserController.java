package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageInfo;

@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
    	logger.info("访问首页");
        return "index";
    }

    @ResponseBody
    @GetMapping("/api/users")
    public PageInfo<User> getUsers(@RequestParam("page") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email) {
    	logger.info("查询用户列表: page={}, size={}, name={}, email={}", page, size, name, email);
        return userService.findUsers(page, size, name, email);
    }
    
    // 新增用户
    @ResponseBody
    @PostMapping("/api/users")
    public User addUser(@RequestBody User user) {
    	logger.info("添加用户: {}", user);
        userService.addUser(user);
        return user;
    }

    // 修改用户
    @ResponseBody
    @PutMapping("/api/users/{id}")
    public User updateUser(@PathVariable("id") Integer id, @RequestBody User user) {
    	logger.info("更新用户: {}", user);
        user.setId(id);
        userService.updateUser(user);
        return user;
    }

    // 删除用户
    @ResponseBody
    @DeleteMapping("/api/users/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
    	logger.warn("删除用户 ID: {}", id);
        userService.deleteUser(id);
        return "success";
    }
}


