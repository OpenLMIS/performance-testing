package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.Product;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

public class ProductDAO {

  NamedParameterJdbcTemplate template;

  String insertQuery = "INSERT INTO products (" +
      "code,  primaryName, dispensingUnit, dosesPerDispensingUnit, packSize, active, fullSupply, tracer, roundToZero, packRoundingThreshold) VALUES(" +
      ":code,:primaryName,:dispensingUnit,:dosesPerDispensingUnit,:packSize,:active,:fullSupply,:tracer,:roundToZero,:packRoundingThreshold)";

  public ProductDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insert(Product product) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertQuery, new BeanPropertySqlParameterSource(product), keyHolder, new String[]{"id"});
    return keyHolder.getKey().longValue();
  }
}
