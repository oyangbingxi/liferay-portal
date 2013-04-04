/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.portal.upgrade.v6_2_0;

import com.liferay.portal.kernel.dao.jdbc.DataAccess;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Daniela Zapata Riesco
 */
public class UpgradePasswordPolicy extends UpgradeProcess {

	@Override
	protected void doUpgrade() throws Exception {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = DataAccess.getUpgradeOptimizedConnection();

			ps = con.prepareStatement(
				"select passwordPolicyId from PasswordPolicy where " +
					"uuid_ is null");

			rs = ps.executeQuery();

			while (rs.next()) {
				long passwordPolicyId = rs.getLong("passwordPolicyId");

				runSQL(
					"update PasswordPolicy set uuid_ = '" +
						PortalUUIDUtil.generate() +
							"' where passwordPolicyId = " + passwordPolicyId);
			}
		}
		finally {
			DataAccess.cleanUp(con, ps, rs);
		}
	}

}