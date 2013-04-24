/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.*;
import org.openlmis.performancetesting.dao.FacilityDAO;
import org.openlmis.performancetesting.dao.ProductDAO;
import org.openlmis.performancetesting.helper.FacilityHelper;
import org.openlmis.performancetesting.helper.ProductHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Runner {

  ProductHelper productHelper = new ProductHelper();
  FacilityHelper facilityHelper = new FacilityHelper();

  ProductDAO productDAO;
  FacilityDAO facilityDAO;

  public Runner() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    productDAO = (ProductDAO) ctx.getBean("productDAO");
    facilityDAO = (FacilityDAO) ctx.getBean("facilityDAO");
  }


  public static void main(String[] args) {
    Runner runner = new Runner();
    runner.insertData();
  }

  private void insertData() {
    insertProductData();
    insertFacilityData();


  }

  private Facility insertFacilityData() {

    GeographicLevel geoLevel = facilityHelper.createGeoLevel();
    facilityDAO.insertGeoLevel(geoLevel);

    GeographicZone geoZone = facilityHelper.createGeographicZone(geoLevel, new GeographicZone());
    facilityDAO.insertGeoZone(geoZone);

    FacilityType facilityType = facilityHelper.createFacilityType();
    facilityDAO.insertFacilityType(facilityType);

    FacilityOperator facilityOperator = facilityHelper.createFacilityOperator();

    Facility facility = facilityHelper.createFacility(geoZone, facilityType, facilityOperator);
    facilityDAO.insertFacility(facility);

    System.out.println(facility.getId());
    return facility;
  }

  private void insertProductData() {
    ProductForm productForm = productHelper.createForm();
    productDAO.insertProductForm(productForm);

    DosageUnit dosageUnit = productHelper.createDosageUnit();
    productDAO.insertDosageUnit(dosageUnit);

    ProductCategory category = productHelper.createCategory();
    productDAO.insertCategory(category);

    Product product = productHelper.createProduct(productForm, dosageUnit, category);
    long productId = productDAO.insertProduct(product);

    System.out.println(productId);
  }

}
