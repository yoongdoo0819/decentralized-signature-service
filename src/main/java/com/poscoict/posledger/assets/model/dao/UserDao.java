package com.poscoict.posledger.assets.model.dao;

import com.poscoict.posledger.assets.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> listForBeanPropertyRowMapper() {
        String query = "SELECT * FROM user";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<User>(User.class));
    }

    public int insert(User user) {
        String query = "INSERT INTO User(userid, addr, password, email) VALUES(?, ?, ?, ?)";
        log.info(user.getId() + " " + user.getPassword());
        return jdbcTemplate.update(query, user.getId(), user.getAddr(), user.getPassword(), user.getEmail());
    }

    public /*User*/Map<String, Object> getUser(String userId, String passwd) {


        return jdbcTemplate.queryForMap("select * from User where userid = ? and password = ?", userId, passwd);

        //ResultSet rs;
        /*try {
            return (User) jdbcTemplate.queryForObject("select * from user where id = ?", new Object[]{userId}, new User(rs.getId(), rs.getPasswd()));
        } catch(Exception e) {

        }*/

        //return null;
    }

    public Map<String, Object> getUserByUserId(String userId) {

        return jdbcTemplate.queryForMap("select * from User where userid = ?", userId);
    }
}
