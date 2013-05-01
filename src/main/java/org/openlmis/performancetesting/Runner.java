/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.*;
import org.openlmis.performancetesting.builder.*;
import org.openlmis.performancetesting.dao.*;
import org.openlmis.rnr.domain.ProgramRnrTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openlmis.performancetesting.DateUtil.periodEndDate;
import static org.openlmis.performancetesting.DateUtil.periodStartDate;

public class Runner {

  public static final int STATES_PER_COUNTRY = 10; // 35
  public static final int DISTRICT_PER_STATE = 5; // 25
  public static final int FACILITIES_PER_DISTRICT = 5; // 28
  public static final int FACILITIES_PER_REQUISITION_GROUP = 5; // 100


  private FacilityBuilder facilityBuilder = new FacilityBuilder();
  private ProgramRnrTemplateBuilder programRnrTemplateBuilder = new ProgramRnrTemplateBuilder();
  private UserBuilder userBuilder = new UserBuilder();
  private SupervisoryNodeBuilder supervisoryNodeBuilder = new SupervisoryNodeBuilder();
  private RequisitionGroupBuilder requisitionGroupBuilder = new RequisitionGroupBuilder();
  private ProcessingScheduleBuilder scheduleBuilder = new ProcessingScheduleBuilder();
  private SupplyLineBuilder supplyLineBuilder = new SupplyLineBuilder();

  private ProductData productData = new ProductData();
  private ProgramData programData = new ProgramData();
  private RoleRightData roleRightData = new RoleRightData();
  private FacilityDAO facilityDAO;
  private ProgramRnrTemplateDAO programRnrTemplateDAO;
  private UserDAO userDAO;
  private SupervisoryNodeDAO supervisoryNodeDAO;
  private RequisitionGroupDAO requisitionGroupDAO;
  private ProcessingScheduleDAO processingScheduleDAO;
  private SupplyLineDAO supplyLineDAO;

  private final ArrayList<ProcessingPeriod> periodList = new ArrayList<>();
  private List<Program> programList;
  private ArrayList<Role> rolesList = new ArrayList<>();
  List<GeographicLevel> zoneLevels = new ArrayList<>();
  private Vendor vendor;
  private ProcessingSchedule monthlySchedule;
  private ProcessingSchedule quarterlySchedule;
  private List<ProductForm> productForms = new ArrayList<>();
  private List<DosageUnit> dosageUnits = new ArrayList<>();
  private List<ProductCategory> productCategories = new ArrayList<>();

  public Runner() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    facilityDAO = (FacilityDAO) ctx.getBean("facilityDAO");
    programRnrTemplateDAO = (ProgramRnrTemplateDAO) ctx.getBean("programRnrTemplateDAO");
    userDAO = (UserDAO) ctx.getBean("userDAO");
    supervisoryNodeDAO = (SupervisoryNodeDAO) ctx.getBean("supervisoryNodeDAO");
    requisitionGroupDAO = (RequisitionGroupDAO) ctx.getBean("requisitionGroupDAO");
    processingScheduleDAO = (ProcessingScheduleDAO) ctx.getBean("processingScheduleDAO");
    supplyLineDAO = (SupplyLineDAO) ctx.getBean("supplyLineDAO");

  }


  public static void main(String[] args) throws ParseException {

    Runner runner = new Runner();
    runner.setupReferenceData();
    runner.insertData();
  }

  private void setupReferenceData() {
    dosageUnits = productData.setupDosageUnits();
    productCategories = productData.setupProductCategories();
    productForms = productData.setupProductForms();
    programList = programData.setupPrograms();
    rolesList = roleRightData.setupRoleRights();
    setupVendors();

    insertZoneLevels();
    insertGeoZones();
    insertSchedulesAndPeriods();
    insertRnrTemplate();
  }

  private void insertData() {

    FacilityType facilityType = insertFacilityType();
    //TODO insert facility operator
    FacilityOperator facilityOperator = facilityBuilder.createFacilityOperator();


    Product product = productData.insertProduct(productForms.get(0), dosageUnits.get(0), productCategories.get(0));
    ProgramProduct programProduct = productData.insertProgramProduct(programList.get(0), product);
    productData.insertFacilityApprovedProduct(facilityType, programProduct);

    List<Facility> facilityList = new ArrayList<>();
    for (int facilityCounter = 0; facilityCounter < STATES_PER_COUNTRY * DISTRICT_PER_STATE * FACILITIES_PER_DISTRICT; facilityCounter++) {
      GeographicZone geoZone = facilityDAO.getDistrictZone(facilityCounter % (STATES_PER_COUNTRY * DISTRICT_PER_STATE));
      Facility facility = insertFacility(geoZone, facilityType, facilityOperator);
      facilityList.add(facility);
      insertPairOfUserWithCreateAndAuthorizeRights(facility);

      if ((facilityCounter + 1) % FACILITIES_PER_REQUISITION_GROUP == 0) {
        SupervisoryNode supervisoryNode = insertSupervisoryNodePair(facility);
        RequisitionGroup requisitionGroup = insertRequisitionGroup(supervisoryNode);
        insertRequisitionGroupMember(requisitionGroup, facilityList);
        for (Program program : programList) {
          insertSupplyLine(supervisoryNode, program, facility);
          insertRequisitionGroupProgramSchedule(requisitionGroup, program, monthlySchedule, facility);
          createApproverAtSupervisoryNodes(supervisoryNode);
        }
        facilityList = new ArrayList<>();
      }
    }

  }

  private void createApproverAtSupervisoryNodes(SupervisoryNode supervisoryNode) {
    User user1 = userBuilder.createUser(null, vendor);
    User user2 = userBuilder.createUser(null, vendor);
    userDAO.insertUser(user1);
    userDAO.insertUser(user2);
    Role lmu = rolesList.get(3);
    Role facilityHead = rolesList.get(4);

    for (Program program : programList) {
      RoleAssignment roleAssignment1 = userBuilder.createRoleAssignment(program, user1, lmu, supervisoryNode);
      userDAO.insertRoleAssignment(roleAssignment1);

      RoleAssignment roleAssignment2 = userBuilder.createRoleAssignment(program, user2, facilityHead, supervisoryNode.getParent());
      userDAO.insertRoleAssignment(roleAssignment2);
    }

  }

  private RequisitionGroupProgramSchedule insertRequisitionGroupProgramSchedule(RequisitionGroup requisitionGroup,
                                                                                Program program,
                                                                                ProcessingSchedule monthlySchedule,
                                                                                Facility facility) {
    RequisitionGroupProgramSchedule rgProgramSchedule = requisitionGroupBuilder.createRequisitionGroupProgramSchedule(requisitionGroup, program, monthlySchedule, facility);
    requisitionGroupDAO.insertRequisitionGroupProgramSchedule(rgProgramSchedule);
    return rgProgramSchedule;
  }

  private SupplyLine insertSupplyLine(SupervisoryNode supervisoryNode, Program program, Facility facility) {
    SupplyLine supplyLine = supplyLineBuilder.createSupplyLine(supervisoryNode, program, facility);

    supplyLineDAO.insertSupplyLine(supplyLine);
    return supplyLine;
  }

  private RequisitionGroup insertRequisitionGroup(SupervisoryNode supervisoryNode) {
    RequisitionGroup requisitionGroup = requisitionGroupBuilder.createRequisitionGroup(supervisoryNode);
    requisitionGroupDAO.insertRequisitionGroup(requisitionGroup);
    return requisitionGroup;
  }

  private void insertRequisitionGroupMember(RequisitionGroup requisitionGroup, List<Facility> facilityList) {
    for (Facility facility : facilityList) {
      RequisitionGroupMember rgMember = requisitionGroupBuilder.createRequisitionGroupMember(requisitionGroup, facility);
      requisitionGroupDAO.insertRequisitionMember(rgMember);
    }
  }

  private void insertSchedulesAndPeriods() {
    monthlySchedule = scheduleBuilder.createSchedule("MONTHLY", "monthly");
    processingScheduleDAO.insertSchedule(monthlySchedule);

    quarterlySchedule = scheduleBuilder.createSchedule("QUARTERLY", "QUARTERLY");
    processingScheduleDAO.insertSchedule(quarterlySchedule);

    for (int year = 2012; year < 2014; year++) {
      for (int month = 1; month <= 12; month++) {
        Date periodStartDate = periodStartDate(year, month);
        Date periodEndDate = periodEndDate(year, month);
        ProcessingPeriod period = scheduleBuilder.createPeriod(periodStartDate, periodEndDate, monthlySchedule);
        periodList.add(period);
      }
    }

    for (ProcessingPeriod period : periodList) {
      processingScheduleDAO.insertPeriod(period);
    }
  }

  private SupervisoryNode insertSupervisoryNodePair(Facility facility) {
    SupervisoryNode parentNode = supervisoryNodeBuilder.createSupervisoryNode(facility, null);
    supervisoryNodeDAO.insertSupervisoryNode(parentNode);

    SupervisoryNode childNode = supervisoryNodeBuilder.createSupervisoryNode(facility, parentNode);
    supervisoryNodeDAO.insertSupervisoryNode(childNode);

    return childNode;
  }

  public void setupVendors() {
    vendor = new Vendor("openLmis", true);
    userDAO.insertVendor(vendor);
  }

  private void insertPairOfUserWithCreateAndAuthorizeRights(Facility facility) {
    Role storeInCharge = rolesList.get(2);
    Role facilityHead = rolesList.get(4);

    for (int i = 0; i < 2; i++) {
      User user = userBuilder.createUser(facility, vendor);
      userDAO.insertUser(user);

      for (Program program : programList) {
        userDAO.insertRoleAssignment(userBuilder.createRoleAssignment(program, user, storeInCharge, null));
        userDAO.insertRoleAssignment(userBuilder.createRoleAssignment(program, user, facilityHead, null));
      }
    }
  }


  private void insertRnrTemplate() {
    for (Program program : programList) {
      ProgramRnrTemplate rnrTemplate = programRnrTemplateBuilder.createProgramRnrTemplate(program);
      programRnrTemplateDAO.insertRnrTemplate(rnrTemplate);
    }
  }


  private void insertZoneLevels() {
    zoneLevels.add(facilityBuilder.createGeoLevel("Country", 1));
    zoneLevels.add(facilityBuilder.createGeoLevel("State", 2));
    zoneLevels.add(facilityBuilder.createGeoLevel("District", 3));

    for (GeographicLevel level : zoneLevels) {
      facilityDAO.insertGeoLevel(level);
    }
  }

  private void insertGeoZones() {
    GeographicZone nullZone = new GeographicZone();
    GeographicZone country = facilityBuilder.createGeographicZone(zoneLevels.get(0), nullZone);
    facilityDAO.insertGeoZone(country);
    for (int stateCounter = 0; stateCounter < STATES_PER_COUNTRY; stateCounter++) {
      GeographicZone state = facilityBuilder.createGeographicZone(zoneLevels.get(1), country);
      facilityDAO.insertGeoZone(state);

      for (int districtCounter = 0; districtCounter < DISTRICT_PER_STATE; districtCounter++) {
        GeographicZone district = facilityBuilder.createGeographicZone(zoneLevels.get(2), state);
        facilityDAO.insertGeoZone(district);
      }
    }
  }

  private Facility insertFacility(GeographicZone geoZone, FacilityType facilityType, FacilityOperator facilityOperator) {

    Facility facility = facilityBuilder.createFacility(geoZone, facilityType, facilityOperator);
    facilityDAO.insertFacility(facility);

    return facility;
  }

  private FacilityType insertFacilityType() {
    FacilityType facilityType = facilityBuilder.createFacilityType();
    facilityDAO.insertFacilityType(facilityType);
    return facilityType;
  }

}
