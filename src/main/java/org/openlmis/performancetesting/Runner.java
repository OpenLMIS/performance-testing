/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.*;
import org.openlmis.performancetesting.dao.*;
import org.openlmis.performancetesting.helper.*;
import org.openlmis.rnr.domain.ProgramRnrTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.openlmis.core.domain.Right.*;

public class Runner {

  ProductBuilder productBuilder = new ProductBuilder();
  FacilityBuilder facilityBuilder = new FacilityBuilder();
  ProgramBuilder programBuilder = new ProgramBuilder();
  ProgramRnrTemplateBuilder programRnrTemplateBuilder = new ProgramRnrTemplateBuilder();
  UserBuilder userBuilder = new UserBuilder();
  SupervisoryNodeBuilder supervisoryNodeBuilder = new SupervisoryNodeBuilder();

  ProductDAO productDAO;
  FacilityDAO facilityDAO;
  ProgramDAO programDAO;
  ProgramRnrTemplateDAO programRnrTemplateDAO;
  UserDAO userDAO;
  SupervisoryNodeDAO supervisoryNodeDAO;

  List<Program> programList;
  ArrayList<Role> rolesList;
  private Vendor vendor;

  public Runner() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    productDAO = (ProductDAO) ctx.getBean("productDAO");
    facilityDAO = (FacilityDAO) ctx.getBean("facilityDAO");
    programDAO = (ProgramDAO) ctx.getBean("programDAO");
    programRnrTemplateDAO = (ProgramRnrTemplateDAO) ctx.getBean("programRnrTemplateDAO");
    userDAO = (UserDAO) ctx.getBean("userDAO");
    supervisoryNodeDAO = (SupervisoryNodeDAO) ctx.getBean("supervisoryNodeDAO");

  }


  public static void main(String[] args) {
    Runner runner = new Runner();
    runner.insertData();
  }

  private void insertData() {
    insertSupervisoryNodePair(insertFacilityData());
    insertVendor();
    insertUserData();
    insertPrograms();
    insertRnrTemplate();
    insertProductData();
    insertFacilityData();
  }

  private void insertSupervisoryNodePair(Facility facility) {
    SupervisoryNode parentNode = supervisoryNodeBuilder.createSupervisoryNode(facility, null);
    supervisoryNodeDAO.insertSupervisoryNode(parentNode);

    SupervisoryNode childNode = supervisoryNodeBuilder.createSupervisoryNode(facility, parentNode);
    supervisoryNodeDAO.insertSupervisoryNode(childNode);
  }

  public void insertVendor() {
    vendor = new Vendor("openLmis", true);
    userDAO.insertVendor(vendor);
  }

  private void insertUserData() {
    insertRoleRights();
    userDAO.insertUser(userBuilder.createUser(insertFacilityData(), vendor));
  }

  private void insertRoleRights() {
    rolesList = new ArrayList<Role>() {{
      add(userBuilder.createRole("ADMIN", true, asList(UPLOADS, MANAGE_FACILITY, MANAGE_ROLE, MANAGE_USERS, MANAGE_SCHEDULE, CONFIGURE_RNR)));
      add(userBuilder.createRole("LMU In-Charge", true, asList(CONVERT_TO_ORDER, VIEW_ORDER)));
      add(userBuilder.createRole("Store In-Charge", false, asList(VIEW_REQUISITION, CREATE_REQUISITION)));
      add(userBuilder.createRole("LMU", false, asList(VIEW_REQUISITION, APPROVE_REQUISITION)));
      add(userBuilder.createRole("FacilityHead", false, asList(VIEW_REQUISITION, AUTHORIZE_REQUISITION)));
      add(userBuilder.createRole("Medical-Officer", false, asList(VIEW_REQUISITION, APPROVE_REQUISITION)));
    }};

    for (Role role : rolesList) {
      userDAO.insertRoleAndRoleRights(role);
    }
  }

  private void insertRnrTemplate() {
    for (Program program : programList) {
      ProgramRnrTemplate rnrTemplate = programRnrTemplateBuilder.createProgramRnrTemplate(program);
      programRnrTemplateDAO.insertRnrTemplate(rnrTemplate);
    }
  }

  private void insertPrograms() {
    programList = new ArrayList<Program>() {{
      add(programBuilder.createProgram("ESS MEDICINES"));
      add(programBuilder.createProgram("TB"));
      add(programBuilder.createProgram("MALARIA"));
      add(programBuilder.createProgram("ARV/ART"));
      add(programBuilder.createProgram("VACCINES"));
    }};

    for (Program program : programList) {
      programDAO.insertProgram(program);
    }
  }

  private Facility insertFacilityData() {

    GeographicLevel geoLevel = facilityBuilder.createGeoLevel();
    facilityDAO.insertGeoLevel(geoLevel);

    GeographicZone geoZone = facilityBuilder.createGeographicZone(geoLevel, new GeographicZone());
    facilityDAO.insertGeoZone(geoZone);

    FacilityType facilityType = facilityBuilder.createFacilityType();
    facilityDAO.insertFacilityType(facilityType);

    FacilityOperator facilityOperator = facilityBuilder.createFacilityOperator();

    Facility facility = facilityBuilder.createFacility(geoZone, facilityType, facilityOperator);
    facilityDAO.insertFacility(facility);

    System.out.println(facility.getId());
    return facility;
  }

  private void insertProductData() {
    ProductForm productForm = productBuilder.createForm();
    productDAO.insertProductForm(productForm);

    DosageUnit dosageUnit = productBuilder.createDosageUnit();
    productDAO.insertDosageUnit(dosageUnit);

    ProductCategory category = productBuilder.createCategory();
    productDAO.insertCategory(category);

    Product product = productBuilder.createProduct(productForm, dosageUnit, category);
    long productId = productDAO.insertProduct(product);

    System.out.println(productId);
  }

}
