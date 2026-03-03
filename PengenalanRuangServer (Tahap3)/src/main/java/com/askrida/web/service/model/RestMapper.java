package com.askrida.web.service.model;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RestMapper implements RowMapper<RestResult> {
    @Override
    public RestResult mapRow(ResultSet rs, int rowNum) throws SQLException {
        Rest rest = new Rest();
        rest.setId(rs.getInt("id"));
        rest.setKey(rs.getString("key"));
        rest.setValue(rs.getString("value"));
        rest.setNama(rs.getString("nama"));

        RestResult restResult = new RestResult();
        restResult.setId(rs.getInt("id"));
        restResult.setRand(rs.getInt("rand"));
        restResult.setKey(rs.getString("key"));
        restResult.setValue(rs.getString("value"));
        restResult.setRand(rs.getInt("rand"));
        restResult.setNama(rs.getString("nama"));
        restResult.setWaktu_input(rs.getTimestamp("waktu_input"));
        return restResult;
    }
}