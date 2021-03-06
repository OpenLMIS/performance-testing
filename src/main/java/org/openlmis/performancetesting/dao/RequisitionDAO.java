/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.rnr.domain.Rnr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static java.sql.Types.VARCHAR;


@Component
@NoArgsConstructor
public class RequisitionDAO {

  public static final Logger logger = LoggerFactory.getLogger(RequisitionDAO.class);

  @Autowired
  NamedParameterJdbcTemplate template;

  final String insertRequisitionQuery = "INSERT INTO requisitions VALUES(DEFAULT, :facility.id, :program.id, :period.id, " +
      ":status, :fullSupplyItemsSubmittedCost.value, :NonFullSupplyItemsSubmittedCost.value, :supervisoryNodeId, " +
      ":supplyingFacility.id, :submittedDate, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";


  public long insertRequisition(Rnr rnr) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    BeanPropertySqlParameterSource paramSource = new BeanPropertySqlParameterSource(rnr);
    paramSource.registerSqlType("status", VARCHAR);
    template.update(insertRequisitionQuery, paramSource, keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    rnr.setId(id);

    logger.debug("{} requisition {} ", new Date(), id);
    return id;
  }

}
