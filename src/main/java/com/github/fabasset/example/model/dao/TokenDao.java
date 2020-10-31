package com.github.fabasset.example.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TokenDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(int _tokenid) {
        String query = "INSERT INTO Token(tokenid) VALUES(?)";
        return this.jdbcTemplate.update(query, _tokenid);
    }

    public Map<String, Object> getTokenNum() throws Exception {

        return jdbcTemplate.queryForMap("select auto_increment from information_schema.tables where table_name = 'Token'");
    }

}
