/**
 * Copyright (C) 2019-2025 Ubiqube.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.ubiqube.etsi.mano.liquibase.common;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

@Testcontainers
class PostgresqlTest {

	@Container
	private final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
			.withDatabaseName("testdb")
			.withUsername("test")
			.withPassword("test");

	private Connection connection;

	@BeforeEach
	public void setUp() throws SQLException {
		connection = DriverManager.getConnection(
				postgreSQLContainer.getJdbcUrl(),
				postgreSQLContainer.getUsername(),
				postgreSQLContainer.getPassword());
	}

	@AfterEach
	public void tearDown() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	@Test
	void testLiquibaseChangeset() throws Exception {
		final Database database = DatabaseFactory.getInstance()
				.findCorrectDatabaseImplementation(new JdbcConnection(connection));
		final Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.yaml", new ClassLoaderResourceAccessor(),
				database);
		liquibase.update("");
		// Example assertion: Check if a table exists after the changeset is applied
		final boolean tableExists = connection.getMetaData().getTables(null, null, "vnf_package", null).next();
		assertTrue(tableExists, "The table should exist after applying the changeset.");
	}
}
