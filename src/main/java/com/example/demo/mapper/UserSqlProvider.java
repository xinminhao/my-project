package com.example.demo.mapper;

import java.util.Map;

public class UserSqlProvider {
    public String findUsers(Map<String, Object> params) {
        String name = (String) params.get("name");
        String email = (String) params.get("email");

        StringBuilder sql = new StringBuilder("SELECT * FROM users WHERE 1=1 ");
        if (name != null && !name.isEmpty()) {
            sql.append("AND name LIKE CONCAT('%', #{name}, '%') ");
        }
        if (email != null && !email.isEmpty()) {
            sql.append("AND email LIKE CONCAT('%', #{email}, '%') ");
        }
        return sql.toString();
    }
}
