package com.github.fabasset.example.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SigDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(String _sigid, String _path, int _tokenid, String time) {
        String query = "INSERT INTO Sig(sigid, Path, sigTokenId, time) VALUES(?, ?, ?, ?)";
        return this.jdbcTemplate.update(query, _sigid, _path, _tokenid, time);
    }

    public Map<String, Object> getSigBySigNum(int _sigNum) throws Exception {

        return jdbcTemplate.queryForMap("select * from Sig where sigNum = ?", _sigNum);
    }

    public Map<String, Object> getSigBySigid(String _sigid) throws Exception {

        return jdbcTemplate.queryForMap("select * from Sig where sigid = ?", _sigid);
    }

    public Map<String, Object> getSigBySigTokenId(int _sigTokenId) throws Exception {

        return jdbcTemplate.queryForMap("select * from Sig where sigTokenId = ?", _sigTokenId);
    }


}
