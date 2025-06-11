package com.example.demo.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    
    public String deleteUsersByIds(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Integer> ids = (List<Integer>) params.get("ids");
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM users WHERE id IN (");
        sb.append(ids.stream().map(String::valueOf).collect(Collectors.joining(",")));
        sb.append(")");
        return sb.toString();
    }
}