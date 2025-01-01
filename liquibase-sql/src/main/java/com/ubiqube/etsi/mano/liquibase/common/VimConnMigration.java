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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.ubiqube.etsi.mano.liquibase.common.entities.KeystoneAuthV3;
import com.ubiqube.etsi.mano.liquibase.common.entities.OpenstackV3InterfaceInfo;

import liquibase.change.custom.CustomSqlChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.InsertStatement;
import liquibase.statement.core.UpdateStatement;

/**
 * @since 4.0.0
 */
public class VimConnMigration implements CustomSqlChange {

	@Override
	public String getConfirmationMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUp() throws SetupException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setFileOpener(final ResourceAccessor resourceAccessor) {
		// TODO Auto-generated method stub

	}

	@Override
	public ValidationErrors validate(final Database database) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SqlStatement[] generateStatements(final Database database) throws CustomChangeException {
		List<SqlStatement> res = handleAccessInfo(database);
		List<SqlStatement> all = new ArrayList<>(res);
		res = handleInterfaceInfo(database);
		all.addAll(res);
		return all.toArray(new SqlStatement[0]);
	}

	private static List<SqlStatement> handleInterfaceInfo(Database database) throws CustomChangeException {
		final Map<UUID, OpenstackV3InterfaceInfo> tuples = new HashMap<>();
		final JdbcConnection connection = (JdbcConnection) database.getConnection();
		try {
			final ResultSet sysConn = connection.createStatement().executeQuery("""
					select
						id, interface_info, interface_info_key
						from vim_connection_information_interface_info vciii join vim_connection_information vci on vciii.vim_connection_information_id = vci.id
					""");
			while (sysConn.next()) {
				final UUID id = sysConn.getObject("id", UUID.class);
				String key = sysConn.getString("interface_info_key");
				String value = sysConn.getString("interface_info");
				final OpenstackV3InterfaceInfo i3 = tuples.computeIfAbsent(id, OpenstackV3InterfaceInfo::new);
				setInterfaceValue(i3, key, value);
			}
		} catch (DatabaseException | SQLException e) {
			throw new CustomChangeException(e);
		}
		return tuples.values().stream().flatMap(x -> {
			List<SqlStatement> ret = new ArrayList<>();
			InsertStatement ins = new InsertStatement(database.getDefaultCatalogName(), database.getDefaultSchemaName(), "openstackv3interface_info")
					.addColumnValue("id", x.getId().toString())
					.addColumnValue("endpoint", x.getEndpoint())
					.addColumnValue("read_timeout", x.getReadTimeout())
					.addColumnValue("connection_timeout", x.getConnectionTimeout())
					.addColumnValue("retry", x.getRetry())
					.addColumnValue("skip_certificate_hostname_check", false)
					.addColumnValue("non_strict_ssl", x.isNonStrictSsl());
			ret.add(ins);
			UpdateStatement upd = new UpdateStatement(database.getDefaultCatalogName(), database.getDefaultSchemaName(), "vim_connection_information")
					.addNewColumnValue("interface_info_id", x.getId().toString());
			ret.add(upd);
			return ret.stream();
		}).toList();

	}

	private static void setInterfaceValue(OpenstackV3InterfaceInfo i3, String key, String value) {
		switch (key) {
		case "endpoint" -> i3.setEndpoint(value);
		case "non-strict-ssl" -> i3.setNonStrictSsl(Boolean.valueOf(value));
		default -> throw new IllegalArgumentException("Unexpected value: " + key);
		}

	}

	private static List<SqlStatement> handleAccessInfo(final Database database) throws CustomChangeException {
		final Map<UUID, KeystoneAuthV3> tuples = new HashMap<>();
		final JdbcConnection connection = (JdbcConnection) database.getConnection();
		try {
			final ResultSet sysConn = connection.createStatement().executeQuery("""
					select
						id, vim_connection_information_id, vciai.access_info , vciai.access_info_key
						from vim_connection_information vci join vim_connection_information_access_info vciai on vci.id = vciai.vim_connection_information_id
					""");
			while (sysConn.next()) {
				final UUID id = sysConn.getObject("id", UUID.class);
				String key = sysConn.getString("access_info_key");
				String value = sysConn.getString("access_info");
				final KeystoneAuthV3 k3 = tuples.computeIfAbsent(id, KeystoneAuthV3::new);
				setValue(k3, key, value);
			}
		} catch (DatabaseException | SQLException e) {
			throw new CustomChangeException(e);
		}
		return tuples.values().stream().flatMap(x -> {
			List<SqlStatement> ret = new ArrayList<>();
			InsertStatement ins = new InsertStatement(database.getDefaultCatalogName(), database.getDefaultSchemaName(), "keystone_authv3")
					.addColumnValue("id", x.getId().toString())
					.addColumnValue("password", x.getPassword())
					.addColumnValue("username", x.getUsername())
					.addColumnValue("project", x.getProject())
					.addColumnValue("project_id", x.getProjectId())
					.addColumnValue("project_domain", x.getProjectDomain())
					.addColumnValue("region", x.getRegion())
					.addColumnValue("user_domain", x.getUserDomain());
			ret.add(ins);
			UpdateStatement upd = new UpdateStatement(database.getDefaultCatalogName(), database.getDefaultSchemaName(), "vim_connection_information")
					.addNewColumnValue("access_info_id", x.getId().toString());
			ret.add(upd);
			return ret.stream();
		}).toList();
	}

	private static void setValue(KeystoneAuthV3 k3, String key, String value) {
		switch (key) {
		case "username" -> k3.setUsername(value);
		case "password" -> k3.setPassword(value);
		case "projectId" -> k3.setProjectId(value);
		case "projectDomain" -> k3.setProjectDomain(value);
		case "userDomain" -> k3.setUserDomain(value);
		case "vim_project" -> { // vim_project is an old value used by CISCO but not by us.
		}
		default -> throw new IllegalArgumentException("Unexpected value: " + key);
		}
	}

}
