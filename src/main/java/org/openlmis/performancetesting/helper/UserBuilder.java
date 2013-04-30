/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.*;

import java.util.HashSet;
import java.util.List;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.openlmis.performancetesting.DateUtil.randomDate;

public class UserBuilder {
  public Role createRole(String name, Boolean isAdminRole, List<Right> rights) {
    Role role = new Role();
    role.setAdminRole(isAdminRole);
    role.setName(name);
    role.setDescription(randomAlphanumeric(20));
    role.setRights(new HashSet<>(rights));
    return role;
  }

  public RoleAssignment createRoleAssignment(Program program, User user, Role role) {
    RoleAssignment roleAssignment = new RoleAssignment();

    roleAssignment.setUserId(user.getId());
    roleAssignment.setRoleId(role.getId());
    roleAssignment.setProgramId(program.getId());

    return roleAssignment;
  }

  public User createUser(Facility homeFacility, Vendor vendor) {
    User user = new User();
    user.setUserName(randomAlphanumeric(20));
    user.setPassword(randomAlphanumeric(10));
    user.setFirstName(randomAlphanumeric(35));
    user.setLastName(randomAlphanumeric(35));
    user.setEmployeeId(randomAlphanumeric(20));
    user.setJobTitle(randomAlphanumeric(40));
    user.setJobTitle(randomAlphanumeric(40));
    user.setPrimaryNotificationMethod(randomAlphanumeric(10));
    user.setOfficePhone(randomAlphanumeric(15));
    user.setCellPhone(randomAlphanumeric(15));
    user.setEmail(randomAlphanumeric(25));
    user.setModifiedBy(Integer.valueOf(randomNumeric(5)));
    user.setActive(true);
    user.setSupervisor(new User());
    user.setModifiedDate(randomDate());

    if (homeFacility != null && homeFacility.getId() != null) {
      user.setFacilityId(homeFacility.getId());
    }

    if (vendor != null && vendor.getId() != null) {
      user.setVendorId(vendor.getId());
    }

    return user;
  }
}
