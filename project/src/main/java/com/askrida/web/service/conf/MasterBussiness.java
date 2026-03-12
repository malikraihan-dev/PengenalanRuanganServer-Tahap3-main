package com.askrida.web.service.conf;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MasterBussiness {

	public JdbcTemplate jdbcTemplate;
	protected Logger log = Logger.getLogger(this.getClass());

	@Bean(name = "db1")
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource1() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "jdbcTemplate1")
	public JdbcTemplate jdbcTemplate1(@Qualifier("db1") DataSource ds) throws SQLException {
		jdbcTemplate = new JdbcTemplate(ds);
		return jdbcTemplate;
	}

	// DB2 - Uncomment and configure spring.second-db.* in application.properties to enable
	// @Bean(name = "db2")
	// @ConfigurationProperties(prefix = "spring.second-db")
	// public DataSource dataSource2() {
	// 	return DataSourceBuilder.create().build();
	// }

	// @Bean(name = "jdbcTemplate2")
	// public JdbcTemplate jdbcTemplate2(@Qualifier("db2") DataSource ds) throws SQLException {
	// 	Connection connection = ds.getConnection();
	// 	connection.setAutoCommit(false);
	// 	return new JdbcTemplate(new SingleConnectionDataSource(connection, true));
	// }

}
