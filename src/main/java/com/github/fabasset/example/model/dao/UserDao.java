package com.github.fabasset.example.model.dao;

import com.github.fabasset.example.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Slf4j
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(User user) {
        String query = "INSERT INTO User(userid, addr, password, email) VALUES(?, ?, ?, ?)";
        log.info(user.getId() + " " + user.getPassword());
        return jdbcTemplate.update(query, user.getId(), user.getAddr(), user.getPassword(), user.getEmail());
    }

    public Map<String, Object> getUser(String userId, String passwd) {


        return jdbcTemplate.queryForMap("select * from User where userid = ? and password = ?", userId, passwd);
    }

    public Map<String, Object> getUserByUserId(String userId) {

        return jdbcTemplate.queryForMap("select * from User where userid = ?", userId);
    }
}
