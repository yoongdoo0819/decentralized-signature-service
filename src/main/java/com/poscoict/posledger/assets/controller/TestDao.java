package com.poscoict.posledger.assets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TestDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object>/*List<Doc>*/ getName(int seq) throws Exception {

        return jdbcTemplate.queryForMap("select * from user where id = ?", "test01");
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }
}
