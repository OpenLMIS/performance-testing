/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.SupplyLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class SupplyLineDAO {

  @Autowired
  NamedParameterJdbcTemplate template;
  final String insertSupplyLineQuery = "INSERT INTO supply_lines VALUES(DEFAULT, :description, :supervisoryNode.id," +
      " :program.id, :supplyingFacility.id, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";


  public long insertSupplyLine(SupplyLine supplyLine) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertSupplyLineQuery, new BeanPropertySqlParameterSource(supplyLine), keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    supplyLine.setId(id);
    return id;
  }
}
