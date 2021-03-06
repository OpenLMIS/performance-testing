/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.openlmis.core.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserDAO {

  @Autowired
  NamedParameterJdbcTemplate template;

  final String insertRoleQuery = "INSERT INTO roles VALUES(DEFAULT, :adminRole, :name, :description)";

  final String insertRoleRightQuery = "INSERT INTO role_rights VALUES(:roleId, :rightName)";

  final String insertVendorQuery = "INSERT INTO vendors VALUES(DEFAULT, :name, DEFAULT, :active)";

  final String insertUserQuery = "INSERT INTO users VALUES(DEFAULT, :userName, :password, :firstName, :lastName" +
      ", :employeeId, :jobTitle, :primaryNotificationMethod, :officePhone, :cellPhone, :email, :supervisor.Id" +
      ", :facilityId, :active, :vendorId, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  final String insertRoleAssignmentQuery = "INSERT INTO role_assignments VALUES(:userId, :roleIds[0], :programId, :supervisoryNode.id)";

  public UserDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertUser(User user) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertUserQuery, new BeanPropertySqlParameterSource(user), keyHolder, new String[]{"id"});

    long userId = keyHolder.getKey().longValue();
    user.setId(userId);

    return userId;
  }

  public long insertRoleAndRoleRights(Role role) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertRoleQuery, new BeanPropertySqlParameterSource(role), keyHolder, new String[]{"id"});

    long roleId = keyHolder.getKey().longValue();
    role.setId(roleId);

    for (Right right : role.getRights()) {
      RoleRight roleRight = new RoleRight(roleId, right.name());
      template.update(insertRoleRightQuery, new BeanPropertySqlParameterSource(roleRight));
    }

    return roleId;
  }

  public void insertRoleAssignment(RoleAssignment roleAssignment) {
    template.update(insertRoleAssignmentQuery, new BeanPropertySqlParameterSource(roleAssignment));
  }

  public long insertVendor(Vendor vendor) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertVendorQuery, new BeanPropertySqlParameterSource(vendor), keyHolder, new String[]{"id"});

    long vendorId = keyHolder.getKey().longValue();
    vendor.setId(vendorId);
    return vendorId;

  }

  @Data
  class RoleRight {
    long roleId;
    String rightName;

    RoleRight(long roleId, String rightName) {
      this.roleId = roleId;
      this.rightName = rightName;
    }
  }

}
