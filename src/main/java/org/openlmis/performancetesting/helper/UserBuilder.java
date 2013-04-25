/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.Right;
import org.openlmis.core.domain.Role;

import java.util.HashSet;
import java.util.List;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

public class UserBuilder {
  public Role createRole(String name, Boolean isAdminRole, List<Right> rights) {
    Role role = new Role();
    role.setAdminRole(isAdminRole);
    role.setName(name);
    role.setDescription(randomAlphanumeric(20));
    role.setRights(new HashSet<>(rights));
    return role;
  }
}
