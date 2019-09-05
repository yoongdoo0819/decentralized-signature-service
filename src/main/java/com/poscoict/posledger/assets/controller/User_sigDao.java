package com.poscoict.posledger.assets.controller;

import com.poscoict.posledger.assets.model.User_Sig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class User_sigDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User_Sig> listForBeanPropertyRowMapper(String userid) {
        String query = "SELECT * FROM User_Sig where userid = " + "'" + userid + "'";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<User_Sig>(User_Sig.class));
    }

    public int insert(String userid, int sigNum) {
        String query = "INSERT INTO User_Sig(userid, sigNum) VALUES(?, ?)";
        return this.jdbcTemplate.update(query, userid, sigNum);//sign.getSignID(), sign.getSignPath());
    }

    public Map<String, Object>/*List<Doc>*/ getUserSig(String _userid) throws Exception {

        return jdbcTemplate.queryForMap("select * from User_Sig where userid = ?", _userid);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

    public Map<String, Object>/*List<Doc>*/ getUserid(int _signum) throws Exception {

        return jdbcTemplate.queryForMap("select * from User_Sig where signum = ?", _signum);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }
}
