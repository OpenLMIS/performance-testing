/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ProgramDAO {

  @Autowired
  NamedParameterJdbcTemplate template;
  final String insertProgramQuery = "INSERT INTO programs VALUES(DEFAULT, :code, :name, :description, :active, :templateConfigured)";

  public long insertProgram(Program program) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertProgramQuery, new BeanPropertySqlParameterSource(program), keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    program.setId(id);
    return id;
  }

}
