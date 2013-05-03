/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.ProgramSupported;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class ProgramsSupportedDAO {

  NamedParameterJdbcTemplate template;
  public static final Logger logger = LoggerFactory.getLogger(LineItemDAO.class);

  final String insertProgramsSupported = "INSERT INTO programs_supported VALUES(DEFAULT, :facilityId, :program.id," +
      ":startDate, :active, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  public ProgramsSupportedDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertProgramSupported(ProgramSupported programSupported) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertProgramsSupported, new BeanPropertySqlParameterSource(programSupported), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    programSupported.setId(id);
    return id;
  }

}
