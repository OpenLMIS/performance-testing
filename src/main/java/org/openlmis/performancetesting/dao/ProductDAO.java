package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.DosageUnit;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductCategory;
import org.openlmis.core.domain.ProductForm;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class ProductDAO {

  NamedParameterJdbcTemplate template;

  final String insertProductQuery = "INSERT INTO products VALUES( " +
      "DEFAULT, :code, :alternateItemCode, :manufacturer, :manufacturerCode, :manufacturerBarCode, :mohBarCode, :gtin, :type, :displayOrder, :primaryName, :fullName, :genericName, :alternateName, :description, :strength" +
      ", :form.id, :dosageUnit.id, :category.id" +
      ", :dispensingUnit, :dosesPerDispensingUnit, :packSize, :alternatePackSize, :storeRefrigerated, :storeRoomTemperature, :hazardous, :flammable, :controlledSubstance, :lightSensitive, :approvedByWHO, :contraceptiveCYP, :packLength, :packWidth, :packHeight, :packWeight, :packsPerCarton, :cartonLength, :cartonWidth, :cartonHeight, :cartonsPerPallet, :expectedShelfLife, :specialStorageInstructions, :specialTransportInstructions, :active, :fullSupply, :tracer, :roundToZero, :archived, :packRoundingThreshold, :modifiedBy)";

  final String insertFormQuery = "INSERT INTO product_forms VALUES(DEFAULT, :code, :displayOrder)";
  final String insertDosageUnitQuery = "INSERT INTO dosage_units VALUES(DEFAULT, :code, :displayOrder)";
  final String insertCategoryQuery = "INSERT INTO product_categories VALUES(DEFAULT, :code, :name, :displayOrder, NULL, NULL, NULL, NULL)";

  public ProductDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertProduct(Product product) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertProductQuery, new BeanPropertySqlParameterSource(product), keyHolder, new String[]{"id"});
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

  public int insertCategory(ProductCategory productCategory) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertCategoryQuery, new BeanPropertySqlParameterSource(productCategory), keyHolder, new String[]{"id"});
    int autoGeneratedKey = keyHolder.getKey().intValue();
    productCategory.setId(autoGeneratedKey);
    return autoGeneratedKey;
  }
}
