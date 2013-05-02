/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.rnr.domain.RnrLineItem;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class LineItemDAO {

  NamedParameterJdbcTemplate template;

  final String insertLineItemQuery = "INSERT INTO requisition_line_items VALUES(DEFAULT, :rnrId, :productCode, :product," +
      ":productDisplayOrder, :productCategory, :productCategoryDisplayOrder, :dispensingUnit, :beginningBalance, :quantityReceived," +
      ":quantityDispensed, :stockInHand, :quantityRequested, :reasonForRequestedQuantity, :calculatedOrderQuantity, " +
      ":quantityApproved, :totalLossesAndAdjustments, :newPatientCount, :stockOutDays, " +
      ":normalizedConsumption, :amc, :maxMonthsOfStock, :maxStockQuantity, :packsToShip, :price.value, " +
      ":remarks, :dosesPerMonth, :dosesPerDispensingUnit, :packSize, :roundToZero, :packRoundingThreshold, :fullSupply," +
      ":previousStockInHandAvailable, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  public LineItemDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertLineItem(RnrLineItem lineItem) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertLineItemQuery, new BeanPropertySqlParameterSource(lineItem), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    lineItem.setId(id);
    return id;
  }

}
