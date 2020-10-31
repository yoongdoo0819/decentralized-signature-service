package com.github.fabasset.model.dao;

import com.github.fabasset.model.User_Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class User_docDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User_Doc> listForBeanPropertyRowMapper(String _userid) {
        String query = "SELECT * FROM User_Doc where userid = " + "'" + _userid + "'";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<User_Doc>(User_Doc.class));
    }

    public List<User_Doc> listForBeanPropertyRowMapperByDocNum(int docnum) {
        String query = "SELECT * FROM User_Doc where docnum = " + "'" + docnum + "'";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<User_Doc>(User_Doc.class));
    }

    public int insert(String _userid, int _docnum) {
        String query = "INSERT INTO User_Doc(userid, docnum) VALUES(?, ?)";
        return this.jdbcTemplate.update(query, _userid, _docnum);
    }

}
