package com.poscoict.posledger.assets.controller;

import com.poscoict.posledger.assets.model.Sign;
import com.poscoict.posledger.assets.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
public class SigDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Sign> listForBeanPropertyRowMapper() {
        String query = "SELECT * FROM Sign";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<Sign>(Sign.class));
    }

    public int insert(String _sigid, String _path) {
        String query = "INSERT INTO Sig(sigid, Path) VALUES(?, ?)";
        return this.jdbcTemplate.update(query, _sigid, _path);//sign.getSignID(), sign.getSignPath());
    }

    public Map<String, Object>/*List<Doc>*/ getName(int seq) throws Exception {

        return jdbcTemplate.queryForMap("select * from _sig where _sigid = ?", "1");
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }


}
