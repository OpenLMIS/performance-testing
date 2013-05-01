/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.Role;
import org.openlmis.performancetesting.builder.UserBuilder;
import org.openlmis.performancetesting.dao.UserDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static org.openlmis.core.domain.Right.*;

public class RoleRightData {

  private UserBuilder userBuilder;
  private UserDAO userDAO;

  public RoleRightData() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    userDAO = (UserDAO) ctx.getBean("userDAO");
    userBuilder = new UserBuilder();

  }

  public ArrayList<Role> setupRoleRights() {
    ArrayList<Role> rolesList = new ArrayList<Role>() {{
      add(userBuilder.createRole("ADMIN", true, asList(UPLOADS, MANAGE_FACILITY, MANAGE_ROLE, MANAGE_USERS, MANAGE_SCHEDULE, CONFIGURE_RNR)));
      add(userBuilder.createRole("LMU In-Charge", true, asList(CONVERT_TO_ORDER, VIEW_ORDER)));
      add(userBuilder.createRole("Store In-Charge", false, asList(VIEW_REQUISITION, CREATE_REQUISITION)));
      add(userBuilder.createRole("LMU", false, asList(VIEW_REQUISITION, APPROVE_REQUISITION)));
      add(userBuilder.createRole("FacilityHead", false, asList(VIEW_REQUISITION, AUTHORIZE_REQUISITION)));
      add(userBuilder.createRole("Medical-Officer", false, asList(VIEW_REQUISITION, APPROVE_REQUISITION)));
    }};

    for (Role role : rolesList) {
      userDAO.insertRoleAndRoleRights(role);
    }

    return rolesList;
  }

}
