/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Map;

public class DBCleaner {


  private String truncateQuery = "TRUNCATE vendors, roles, role_rights, users, role_assignments, losses_adjustments_types CASCADE";

  public static void main(String[] args) {
    DBCleaner cleaner = new DBCleaner();
    cleaner.run();
  }

  private void run() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    NamedParameterJdbcTemplate template = (NamedParameterJdbcTemplate) ctx.getBean("template");
    template.update(truncateQuery, (Map<String, ?>) null);
  }
}
