package com.askrida.web.service.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.springframework.jdbc.core.RowMapper;

public class DTOMap implements Serializable, RowMapper {
	private static final long serialVersionUID = -8840406844877458198L;
	public HashMap<String, Object> map = new HashMap<String, Object>();

	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		DTOMap dto = new DTOMap();
		int rowCount = rs.getMetaData().getColumnCount();
		for (int i = 1; i <= rowCount; i++) {
			dto.map.put(rs.getMetaData().getColumnLabel(i), rs.getObject(i));
		}
		return dto;
	}

	public void put(String name, Object o) {
		map.put(name, o);
	}

	public Object get(String name) {
		return map.get(name);
	}

	public String getString(String name) {
		return (String) map.get(name);
	}

	public Integer getInt(String name) {
		return (Integer) map.get(name);
	}

	public Date getDate(String name) {
		return (Date) map.get(name);
	}

	public BigDecimal getBigDecimal(String name) {
		return (BigDecimal) map.get(name);
	}

	public void copyItem(String key, DTOMap map) {
		this.put(key, map.get(key));
	}

	public HashMap<String, Object> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Object> map) {
		this.map = map;
	}

}
