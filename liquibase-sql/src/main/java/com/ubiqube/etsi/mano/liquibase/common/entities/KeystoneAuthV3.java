/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package com.ubiqube.etsi.mano.liquibase.common.entities;

import java.util.UUID;

import lombok.Data;

@Data
public class KeystoneAuthV3 {

	private UUID id = UUID.randomUUID();
	private String username;
	private String password;
	/**
	 * The OpenStack region to use for the VIM connection.
	 */
	private String region = "RegionOne";
	/**
	 * The OpenStack project to use for the VIM connection.
	 */
	private String project = "admin";

	private String projectId;
	/**
	 * The OpenStack project domain to use for the VIM connection.
	 */
	private String projectDomain;
	/**
	 * The OpenStack user domain to use for the VIM connection.
	 */
	private String userDomain;

	public KeystoneAuthV3(final UUID x) {
		this.id = x;
	}

}
