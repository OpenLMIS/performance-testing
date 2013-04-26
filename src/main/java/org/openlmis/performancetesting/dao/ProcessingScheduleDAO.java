/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.ProcessingSchedule;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class ProcessingScheduleDAO {

  NamedParameterJdbcTemplate template;
  final String insertSchedule = "INSERT INTO processing_schedules VALUES(DEFAULT, :code, :name, :description, " +
      ":modifiedBy, :modifiedDate, :modifiedBy)";

  public ProcessingScheduleDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  public int insertSchedule(ProcessingSchedule processingSchedule) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertSchedule, new BeanPropertySqlParameterSource(processingSchedule), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    processingSchedule.setId(id);
    return id;
  }

}
