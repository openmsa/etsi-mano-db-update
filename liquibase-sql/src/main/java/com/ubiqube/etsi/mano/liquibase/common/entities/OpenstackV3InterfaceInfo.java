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
