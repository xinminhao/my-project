package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public PageInfo<User> findUsers(int page, int size, String name, String email) {
        PageHelper.startPage(page, size);
        List<User> users = userMapper.findUsers(name, email);
        return new PageInfo<>(users);
    }
    
    public void addUser(User user) {
        userMapper.insertUser(user);
    }

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }
    
    public void deleteUsersByIds(List<Integer> ids) {
        userMapper.deleteUsersByIds(ids);
    }

}
