package com.poscoict.posledger.assets.controller;

import com.poscoict.posledger.assets.model.Sign;
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

    public List<Sign> listForBeanPropertyRowMapper() {
        String query = "SELECT * FROM Sign";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<Sign>(Sign.class));
    }

    public int insert(String _userid, String _sigid) {
        String query = "INSERT INTO User_Sig(userid, sigid) VALUES(?, ?)";
        return this.jdbcTemplate.update(query, _userid, _sigid);//sign.getSignID(), sign.getSignPath());
    }

    public Map<String, Object>/*List<Doc>*/ getUserSig(String _userid) throws Exception {

        return jdbcTemplate.queryForMap("select * from User_Sig where userid = ?", _userid);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }
}
