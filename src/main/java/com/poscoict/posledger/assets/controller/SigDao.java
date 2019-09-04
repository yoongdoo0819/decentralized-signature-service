package com.poscoict.posledger.assets.controller;

import com.poscoict.posledger.assets.model.Sig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SigDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Sig> listForBeanPropertyRowMapper(String _sigid) {
        String query = "SELECT * FROM Sign where sigid = '" + _sigid + "'";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<Sig>(Sig.class));
    }

    public int insert(String _sigid, String _path, int _tokenid) {
        String query = "INSERT INTO Sig(sigid, Path, sigTokenId) VALUES(?, ?, ?)";
        return this.jdbcTemplate.update(query, _sigid, _path, _tokenid);//sign.getSignID(), sign.getSignPath());
    }

    public Map<String, Object>/*List<Doc>*/ getSigBySigNum(int _sigNum) throws Exception {

        return jdbcTemplate.queryForMap("select * from Sig where sigNum = ?", _sigNum);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

    public Map<String, Object>/*List<Doc>*/ getSigBySigid(String _sigid) throws Exception {

        return jdbcTemplate.queryForMap("select * from Sig where sigid = ?", _sigid);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }




}
