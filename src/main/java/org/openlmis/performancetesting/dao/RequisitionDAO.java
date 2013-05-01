/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.rnr.domain.Rnr;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class RequisitionDAO {

  NamedParameterJdbcTemplate template;

  final String insertRequisitionQuery = "INSERT INTO requisitions VALUES(DEFAULT, :facility.id, :program.id, :period.id, " +
      "'RELEASED', :fullSupplyItemsSubmittedCost.value, :NonFullSupplyItemsSubmittedCost.value, :supervisoryNodeId, " +
      ":supplyingFacility.id, NULL, :submittedDate, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  public RequisitionDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertRequisition(Rnr rnr) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertRequisitionQuery, new BeanPropertySqlParameterSource(rnr), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    rnr.setId(id);
    return id;
  }

}
