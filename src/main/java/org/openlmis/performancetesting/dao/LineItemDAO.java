/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.Program;
import org.openlmis.rnr.domain.LossesAndAdjustments;
import org.openlmis.rnr.domain.LossesAndAdjustmentsType;
import org.openlmis.rnr.domain.RnrLineItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineItemDAO {

  NamedParameterJdbcTemplate template;
  public static final Logger logger = LoggerFactory.getLogger(LineItemDAO.class);

  final String insertLineItemQuery = "INSERT INTO requisition_line_items VALUES(DEFAULT, :rnrId, :productCode, :product," +
      ":productDisplayOrder, :productCategory, :productCategoryDisplayOrder, :dispensingUnit, :beginningBalance, :quantityReceived," +
      ":quantityDispensed, :stockInHand, :quantityRequested, :reasonForRequestedQuantity, :calculatedOrderQuantity, " +
      ":quantityApproved, :totalLossesAndAdjustments, :newPatientCount, :stockOutDays, " +
      ":normalizedConsumption, :amc, :maxMonthsOfStock, :maxStockQuantity, :packsToShip, :price.value, " +
      ":remarks, :dosesPerMonth, :dosesPerDispensingUnit, :packSize, :roundToZero, :packRoundingThreshold, :fullSupply," +
      ":previousStockInHandAvailable, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  final String insertAdjustmentTypeQuery = "INSERT INTO losses_adjustments_types VALUES(:name, :description, :additive, :displayOrder)";
  final String insertAdjustmentsQuery = "INSERT INTO requisition_line_item_losses_adjustments VALUES(:id, :type.name, :quantity, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

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

  public void insertLossesAndAdjustmentType(LossesAndAdjustmentsType adjustmentsType) {
    template.update(insertAdjustmentTypeQuery, new BeanPropertySqlParameterSource(adjustmentsType));
  }


  public void insertLossesAndAdjustments(LossesAndAdjustments lossesAndAdjustments) {
    try {
      template.update(insertAdjustmentsQuery, new BeanPropertySqlParameterSource(lossesAndAdjustments));
    } catch (Exception e) {
      logger.debug(e.getMessage());
    }
  }

  public List<Product> getLineItems(Facility facility, Program program, Boolean isFullSupply) {
    String getLineItemQuery = "SELECT p.code, p.fullSupply " +
        "FROM facility_approved_products fap " +
        "INNER JOIN facilities f ON f.typeId = fap.facilityTypeId " +
        "INNER JOIN program_products pp ON pp.id = fap.programProductId " +
        "INNER JOIN products p ON p.id = pp.productId " +
        "INNER JOIN product_categories pc ON pc.id = p.categoryId " +
        "WHERE " +
        "pp.programId = :programId " +
        "AND f.id = :facilityId " +
        "AND p.fullSupply = :fullSupply " +
        "AND p.active = TRUE AND pp.active = TRUE " +
        "ORDER BY pc.displayOrder, pc.name, p.displayOrder NULLS LAST, p.code";

    Map<String, Object> params = new HashMap<>();
    params.put("programId", program.getId());
    params.put("facilityId", facility.getId());
    params.put("fullSupply", isFullSupply);
    SqlParameterSource namedParameters = new MapSqlParameterSource(params);

    List<Product> products = new ArrayList<>();
    List<Map<String, Object>> values = template.queryForList(getLineItemQuery, namedParameters);

    for (Map<String, Object> record : values) {
      Product product = new Product();
      product.setCode((String) record.get("code"));
      product.setFullSupply((Boolean) record.get("fullSupply"));
      products.add(product);
    }

    return products;

  }

}
