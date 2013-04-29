/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.ProgramProduct;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class ProgramProductDAO {

  NamedParameterJdbcTemplate template;
  final String insertProgramQuery = "INSERT INTO program_products VALUES(DEFAULT, :program.id, :product.id, " +
      ":dosesPerMonth, :active, :currentPrice.value, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  public ProgramProductDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  public int insertProgramProduct(ProgramProduct programProduct) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertProgramQuery, new BeanPropertySqlParameterSource(programProduct), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    programProduct.setId(id);
    return id;
  }

}
