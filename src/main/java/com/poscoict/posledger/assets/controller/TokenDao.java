package com.poscoict.posledger.assets.controller;

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
        return this.jdbcTemplate.update(query, _tokenid);//sign.getSignID(), sign.getSignPath());
    }

    public Map<String, Object>/*List<Doc>*/ getTokenNum() throws Exception {

        return jdbcTemplate.queryForMap("select auto_increment from information_schema.tables where table_name = 'Token'");
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

}
