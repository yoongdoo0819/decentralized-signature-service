package com.poscoict.posledger.assets.controller;

import com.poscoict.posledger.assets.model.User_Doc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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

    public int insert(String _userid, int _docid) {
        String query = "INSERT INTO User_Doc(userid, docnum) VALUES(?, ?)";
        return this.jdbcTemplate.update(query, _userid, _docid);//sign.getSignID(), sign.getSignPath());
    }




    public Map<String, Object>/*List<Doc>*/ getUserDoc(String _userid) throws Exception {

        return jdbcTemplate.queryForMap("select * from User_Doc where userid = ?", _userid);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

}
