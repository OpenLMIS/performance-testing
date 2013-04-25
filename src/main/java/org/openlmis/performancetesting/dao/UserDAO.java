/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.Data;
import org.openlmis.core.domain.Right;
import org.openlmis.core.domain.Role;
import org.openlmis.core.domain.Vendor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class UserDAO {
  NamedParameterJdbcTemplate template;

  final String insertRoleQuery = "INSERT INTO roles VALUES(DEFAULT, :adminRole, :name, :description)";
  final String insertRoleRightQuery = "INSERT INTO role_rights VALUES(:roleId, :rightName)";
  final String insertVendorQuery = "INSERT INTO vendors VALUES(DEFAULT, :name, DEFAULT, :active)";

  public UserDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertRoleAndRoleRights(Role role) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertRoleQuery, new BeanPropertySqlParameterSource(role), keyHolder, new String[]{"id"});

    int roleId = keyHolder.getKey().intValue();
    role.setId(roleId);

    for (Right right : role.getRights()) {
      RoleRight roleRight = new RoleRight(roleId, right.name());
      template.update(insertRoleRightQuery, new BeanPropertySqlParameterSource(roleRight));
    }

    return roleId;
  }

  public int insertVendor(Vendor vendor) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertVendorQuery, new BeanPropertySqlParameterSource(vendor), keyHolder, new String[]{"id"});

    int vendorId = keyHolder.getKey().intValue();
    vendor.setId(vendorId);
    return vendorId;

  }

  @Data
  class RoleRight {
    Integer roleId;
    String rightName;

    RoleRight(Integer roleId, String rightName) {
      this.roleId = roleId;
      this.rightName = rightName;
    }
  }

}
