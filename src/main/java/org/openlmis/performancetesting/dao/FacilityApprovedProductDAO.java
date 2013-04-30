/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.FacilityApprovedProduct;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class FacilityApprovedProductDAO {
  NamedParameterJdbcTemplate template;
  final String insertFacilityApprovedProductQuery = "INSERT INTO facility_approved_products VALUES(DEFAULT, :facilityType.id, " +
      ":programProduct.id, :maxMonthsOfStock, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  public FacilityApprovedProductDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  public int insertFacilityApprovedProduct(FacilityApprovedProduct facilityApprovedProduct) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertFacilityApprovedProductQuery, new BeanPropertySqlParameterSource(facilityApprovedProduct), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    facilityApprovedProduct.setId(id);
    return id;
  }
}
