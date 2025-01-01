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
package com.ubiqube.etsi.mano.liquibase.common.entities;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class OpenstackV3InterfaceInfo {

	private List<String> trustedCertificates;
	/**
	 * Certificate hostname check for the endpoint can be skipped by setting this
	 * field to true.
	 */
	private boolean skipCertificateHostnameCheck;
	/**
	 * Certificate verification for the endpoint can be skipped by setting this
	 * field to true.
	 */
	private String skipCertificateVerification;

	private UUID id = UUID.randomUUID();

	private String endpoint;

	private boolean nonStrictSsl;

	private String natHost;

	/**
	 * Connection timeout in millisecondes.
	 */
	private int connectionTimeout = 5_000;

	/**
	 * Read timeout in millisecondes.
	 */
	private int readTimeout = 5_000;

	/**
	 * Retry on failure.
	 */
	private int retry = 5;

	private String iface;

	private String regionName;

	private String sdnEndpoint;

	public OpenstackV3InterfaceInfo(final UUID id) {
		this.id = id;
	}

}
