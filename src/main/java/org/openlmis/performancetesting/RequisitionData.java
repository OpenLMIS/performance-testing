/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.*;
import org.openlmis.performancetesting.builder.LineItemBuilder;
import org.openlmis.performancetesting.builder.RequisitionBuilder;
import org.openlmis.performancetesting.dao.LineItemDAO;
import org.openlmis.performancetesting.dao.RequisitionDAO;
import org.openlmis.rnr.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.openlmis.performancetesting.Properties.ADJUSTMENT_NAMES;
import static org.openlmis.performancetesting.Properties.NUMBER_OF_NON_FULL_SUPPLY_ITEMS;
import static org.openlmis.performancetesting.Utils.randomInteger;

public class RequisitionData {

  private RequisitionBuilder requisitionBuilder;
  private RequisitionDAO requisitionDAO;
  private final LineItemDAO lineItemDAO;
  private final LineItemBuilder lineItemBuilder;
  private ArrayList<LossesAndAdjustmentsType> adjustmentTypeList;

  public static final Logger logger = LoggerFactory.getLogger(RequisitionData.class);

  public RequisitionData() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    requisitionDAO = (RequisitionDAO) ctx.getBean("requisitionDAO");
    lineItemDAO = (LineItemDAO) ctx.getBean("lineItemDAO");
    requisitionBuilder = new RequisitionBuilder();
    lineItemBuilder = new LineItemBuilder();
  }

  public void createRequisition(List<ProcessingPeriod> periods, Facility facility, Program program,
                                SupervisoryNode supervisoryNode, Facility supplyingFacility, RnrStatus status) {

    for (ProcessingPeriod period : periods) {
      Rnr requisition = requisitionBuilder.createRequisition(facility, program, period, supervisoryNode, supplyingFacility, status);
      requisitionDAO.insertRequisition(requisition);
      createLineItem(requisition);
    }
  }

  public void createRequisition(int facilityCounter, List<ProcessingPeriod> periods, Facility facility,
                                Program program, SupervisoryNode supervisoryNode, Facility supplyingFacility) {
    for (ProcessingPeriod period : periods) {
      RnrStatus status = decideStatus(facilityCounter);
      supervisoryNode = getSupervisoryNode(supervisoryNode, status);

      if (status != null) {
        Rnr requisition = requisitionBuilder.createRequisition(facility, program, period, supervisoryNode, supplyingFacility, status);
        requisitionDAO.insertRequisition(requisition);
        createLineItem(requisition);
      }
    }

  }

  private SupervisoryNode getSupervisoryNode(SupervisoryNode supervisoryNode, RnrStatus status) {
    if (status == RnrStatus.IN_APPROVAL) {
      assert supervisoryNode != null;
      supervisoryNode = supervisoryNode.getParent();
    } else if (status != RnrStatus.AUTHORIZED) {
      supervisoryNode = null;
    }
    return supervisoryNode;
  }

  private RnrStatus decideStatus(int facilityCounter) {
    int statusEnumIndex = (facilityCounter % 6);
    if (statusEnumIndex == 5) return null;
    return RnrStatus.values()[statusEnumIndex];
  }

  public void createLineItem(final Rnr requisition) {
    List<Product> fullSupplyProducts = lineItemDAO.getLineItems(requisition.getFacility(), requisition.getProgram(), true);
    for (int counter = 0; counter < fullSupplyProducts.size(); counter++) {
      RnrLineItem lineItem = lineItemBuilder.createLineItem(requisition, fullSupplyProducts.get(counter));
      lineItemDAO.insertLineItem(lineItem);
      if (counter % 2 == 0) {
        createLossesAndAdjustments(lineItem);
      }
    }

    List<Product> nonFullSupplyProducts = lineItemDAO.getLineItems(requisition.getFacility(), requisition.getProgram(), false);
    int startingIndex = randomInteger(0, nonFullSupplyProducts.size() - NUMBER_OF_NON_FULL_SUPPLY_ITEMS);
    for (int counter = 0; counter < NUMBER_OF_NON_FULL_SUPPLY_ITEMS; counter++) {
      RnrLineItem lineItem = lineItemBuilder.createLineItem(requisition, nonFullSupplyProducts.get(startingIndex++));
      lineItemDAO.insertLineItem(lineItem);
    }
    logger.debug("{},{} products(f,n) for requisition #{}", fullSupplyProducts.size(), nonFullSupplyProducts.size(), requisition.getId());
  }

  private void createLossesAndAdjustments(RnrLineItem lineItem) {
    int totalNoOfAdjustmentsPerProduct = 3;
    int max = adjustmentTypeList.size() - totalNoOfAdjustmentsPerProduct;
    for (int startIndex = randomInteger(0, max), numberOfAdjustments = 0; numberOfAdjustments < totalNoOfAdjustmentsPerProduct; numberOfAdjustments++) {
      LossesAndAdjustmentsType lossesAndAdjustmentsType = adjustmentTypeList.get(startIndex + numberOfAdjustments);
      LossesAndAdjustments le = lineItemBuilder.createLossesAndAdjustment(lineItem, lossesAndAdjustmentsType);
      lineItemDAO.insertLossesAndAdjustments(le);
    }
  }

  public void setupLossesAndAdjustmentTypes() {
    adjustmentTypeList = new ArrayList<>();

    for (String name : ADJUSTMENT_NAMES) {
      LossesAndAdjustmentsType adjustmentType = lineItemBuilder.createAdjustmentType(name);
      lineItemDAO.insertLossesAndAdjustmentType(adjustmentType);
      adjustmentTypeList.add(adjustmentType);
    }
  }
}
