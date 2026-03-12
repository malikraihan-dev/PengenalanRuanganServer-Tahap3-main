package com.askrida.web.service.conf;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RowMapperObject implements RowMapper {

	private int rowCount;

	public RowMapperObject(int rowCount) {
		super();
		this.rowCount = rowCount;
	}

	public RowMapperObject() {
		super();
	}

	public Object mapRow(ResultSet rs, int index) throws SQLException {
		rowCount = rs.getMetaData().getColumnCount();
		Object[] o = new Object[rowCount];
		for (int i = 1; i <= rowCount; i++) {
			o[i - 1] = rs.getObject(i);
		}
		return o;
	}

}
