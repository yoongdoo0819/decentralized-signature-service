package com.github.fabasset.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DocDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insert(String _docid, String _path, int _tokenid, String _signers) {
        String query = "INSERT INTO Doc(docid, path, docTokenId, signers) VALUES(?, ?, ?, ?)";
        return this.jdbcTemplate.update(query, _docid, _path, _tokenid, _signers);
    }

    public Map<String, Object> getDocByDocNum(int _docnum) throws Exception {

        return jdbcTemplate.queryForMap("select * from Doc where docnum = ?", _docnum);
    }

    public Map<String, Object> getDocByDocIdAndNum(String _docid, int _docnum) throws Exception {

        return jdbcTemplate.queryForMap("select * from Doc where docid = ? and docnum = ?", _docid, _docnum);
    }

    public Map<String, Object> getDocNum() throws Exception {

        return jdbcTemplate.queryForMap("select auto_increment from information_schema.tables where table_name = 'Doc'");
    }
}
