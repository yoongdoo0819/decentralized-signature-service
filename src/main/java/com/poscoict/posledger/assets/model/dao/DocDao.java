package com.poscoict.posledger.assets.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.util.List;
import java.util.Map;

@Component
public class DocDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Doc> listForBeanPropertyRowMapperByDocId(String docid) {
        String query = "SELECT * FROM Doc where docid = " + "'" + docid + "'";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

    public int insert(String _docid, String _path, int _tokenid, String _signers, String time) {
        String query = "INSERT INTO Doc(docid, path, docTokenId, signers) VALUES(?, ?, ?, ?, ?)";
        return this.jdbcTemplate.update(query, _docid, _path, _tokenid, _signers, time);//sign.getSignID(), sign.getSignPath());
    }

    public Map<String, Object>/*List<Doc>*/ getDocByDocNum(int _docnum) throws Exception {

        return jdbcTemplate.queryForMap("select * from Doc where docnum = ?", _docnum);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

    public Map<String, Object>/*List<Doc>*/ getDocByDocId(String _docid) throws Exception {

        return jdbcTemplate.queryForMap("select * from Doc where docid = ?", _docid);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

    public Map<String, Object>/*List<Doc>*/ getDocByDocIdAndNum(String _docid, int _docnum) throws Exception {

        return jdbcTemplate.queryForMap("select * from Doc where docid = ? and docnum = ?", _docid, _docnum);
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

    public Map<String, Object>/*List<Doc>*/ getDocNum() throws Exception {

        return jdbcTemplate.queryForMap("select auto_increment from information_schema.tables where table_name = 'Doc'");
        //String query = "select * from test";
        //return jdbcTemplate.query(query, new BeanPropertyRowMapper<Doc>(Doc.class));
    }

}
