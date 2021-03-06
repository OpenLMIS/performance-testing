/*
 * Copyright © 2013 VillageReach.  All Rights Reserved.  This Source Code Form is subject to the terms of the Mozilla Public License, v. 20.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.DosageUnit;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductCategory;
import org.openlmis.core.domain.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;

@Component
@NoArgsConstructor
public class ProductDAO {

  @Autowired
  NamedParameterJdbcTemplate template;

  final String insertProductQuery = format("INSERT INTO products VALUES( " +
      "DEFAULT, :code, :alternateItemCode, :manufacturer, :manufacturerCode, :manufacturerBarCode, :mohBarCode, :gtin" +
      ", :type, :displayOrder, :primaryName, :fullName, :genericName, :alternateName, :description, :strength" +
      ", :form.id, :dosageUnit.id, :category.id" +
      ", :dispensingUnit, :dosesPerDispensingUnit, :packSize, :alternatePackSize, :storeRefrigerated, :storeRoomTemperature" +
      ", :hazardous, :flammable, :controlledSubstance, :lightSensitive, :approvedByWHO, :contraceptiveCYP, :packLength" +
      ", :packWidth, :packHeight, :packWeight, :packsPerCarton, :cartonLength, :cartonWidth, :cartonHeight, :cartonsPerPallet" +
      ", :expectedShelfLife, :specialStorageInstructions, :specialTransportInstructions, :active, :fullSupply" +
      ", :tracer, :roundToZero, :archived, :packRoundingThreshold, :modifiedBy, :modifiedDate, %s, :modifiedDate)", randomNumeric(5));

  final String insertFormQuery = "INSERT INTO product_forms VALUES(DEFAULT, :code, :displayOrder)";
  final String insertDosageUnitQuery = "INSERT INTO dosage_units VALUES(DEFAULT, :code, :displayOrder)";
  final String insertCategoryQuery = "INSERT INTO product_categories VALUES(DEFAULT, :code, :name, :displayOrder)";

  public ProductDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertProduct(Product product) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertProductQuery, new BeanPropertySqlParameterSource(product), keyHolder, new String[]{"id"});

    long autoGeneratedKey = keyHolder.getKey().longValue();
    product.setId(autoGeneratedKey);

    return keyHolder.getKey().longValue();
  }

  public long insertProductForm(ProductForm productForm) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertFormQuery, new BeanPropertySqlParameterSource(productForm), keyHolder, new String[]{"id"});
    long autoGeneratedKey = keyHolder.getKey().longValue();
    productForm.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public long insertDosageUnit(DosageUnit dosageUnit) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertDosageUnitQuery, new BeanPropertySqlParameterSource(dosageUnit), keyHolder, new String[]{"id"});
    long autoGeneratedKey = keyHolder.getKey().longValue();
    dosageUnit.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

  public long insertCategory(ProductCategory productCategory) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertCategoryQuery, new BeanPropertySqlParameterSource(productCategory), keyHolder, new String[]{"id"});
    long autoGeneratedKey = keyHolder.getKey().longValue();
    productCategory.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }

}
