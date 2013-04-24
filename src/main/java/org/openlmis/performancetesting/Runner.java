/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.*;
import org.openlmis.performancetesting.dao.FacilityDAO;
import org.openlmis.performancetesting.dao.ProductDAO;
import org.openlmis.performancetesting.dao.ProgramDAO;
import org.openlmis.performancetesting.dao.ProgramRnrTemplateDAO;
import org.openlmis.performancetesting.helper.FacilityHelper;
import org.openlmis.performancetesting.helper.ProductHelper;
import org.openlmis.performancetesting.helper.ProgramHelper;
import org.openlmis.performancetesting.helper.ProgramRnrTemplateHelper;
import org.openlmis.rnr.domain.ProgramRnrTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Runner {

  ProductHelper productHelper = new ProductHelper();
  FacilityHelper facilityHelper = new FacilityHelper();
  ProgramHelper programHelper = new ProgramHelper();
  ProgramRnrTemplateHelper programRnrTemplateHelper = new ProgramRnrTemplateHelper();

  ProductDAO productDAO;
  FacilityDAO facilityDAO;
  ProgramDAO programDAO;
  ProgramRnrTemplateDAO programRnrTemplateDAO;

  List<Program> programList;

  public Runner() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    productDAO = (ProductDAO) ctx.getBean("productDAO");
    facilityDAO = (FacilityDAO) ctx.getBean("facilityDAO");
    programDAO = (ProgramDAO) ctx.getBean("programDAO");
    programRnrTemplateDAO = (ProgramRnrTemplateDAO) ctx.getBean("programRnrTemplateDAO");
  }


  public static void main(String[] args) {
    Runner runner = new Runner();
    runner.insertData();
  }

  private void insertData() {
    insertPrograms();
    insertRnrTemplate();

    insertProductData();
    insertFacilityData();
  }

  private void insertRnrTemplate() {
    for (Program program : programList) {
      createTemplateForProgram(program);
    }
  }

  private void createTemplateForProgram(Program program) {
    ProgramRnrTemplate programRnrTemplate = programRnrTemplateHelper.createProgramRnrTemplate(program);
    programRnrTemplateDAO.insertRnrTemplate(programRnrTemplate);
  }

  private void insertPrograms() {
    programList = new ArrayList<Program>() {{
      add(programHelper.createProgram("ESS MEDICINES"));
      add(programHelper.createProgram("TB"));
      add(programHelper.createProgram("MALARIA"));
      add(programHelper.createProgram("ARV/ART"));
      add(programHelper.createProgram("VACCINES"));
    }};

    for (Program program : programList) {
      programDAO.insertProgram(program);
    }
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
