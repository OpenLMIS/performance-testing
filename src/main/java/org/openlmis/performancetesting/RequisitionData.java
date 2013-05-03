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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang.math.RandomUtils.nextBoolean;
import static org.openlmis.performancetesting.Constants.ADJUSTMENT_NAMES;
import static org.openlmis.performancetesting.Constants.NUMBER_OF_PERIODS;

public class RequisitionData {

  private RequisitionBuilder requisitionBuilder;
  private RequisitionDAO requisitionDAO;
  private final LineItemDAO lineItemDAO;
  private final LineItemBuilder lineItemBuilder;
  private ArrayList<LossesAndAdjustmentsType> adjustmentTypeList;

  public RequisitionData() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    requisitionDAO = (RequisitionDAO) ctx.getBean("requisitionDAO");
    lineItemDAO = (LineItemDAO) ctx.getBean("lineItemDAO");
    requisitionBuilder = new RequisitionBuilder();
    lineItemBuilder = new LineItemBuilder();
  }

  public void createRequisition(List<ProcessingPeriod> periods, Facility facility, Program program,
                                SupervisoryNode supervisoryNode, Facility supplyingFacility, RnrStatus status) {

    for (int periodIndex = 10; periodIndex < NUMBER_OF_PERIODS && periodIndex < periods.size(); periodIndex++) {
      ProcessingPeriod period = periods.get(periodIndex);
      Rnr requisition = requisitionBuilder.createRequisition(facility, program, period, supervisoryNode, supplyingFacility, status);
      requisitionDAO.insertRequisition(requisition);
      createLineItem(requisition);
    }
  }

  public void createLineItem(final Rnr requisition) {
    List<Product> fullSupplyProducts = lineItemDAO.getLineItems(requisition.getFacility(), requisition.getProgram(), true);
    for (final Product product : fullSupplyProducts) {
      new Thread() {
        @Override
        public void run() {
          RnrLineItem lineItem = lineItemBuilder.createLineItem(requisition, product);
          lineItemDAO.insertLineItem(lineItem);
          if (nextBoolean()) {
            createLossesAndAdjustments(lineItem);
          }
        }
      }.start();
    }

    List<Product> nonFullSupplyProducts = lineItemDAO.getLineItems(requisition.getFacility(), requisition.getProgram(), false);
    for (int counter = 0; counter < nonFullSupplyProducts.size(); counter++) {
      if (counter % 10 == 0) {
        RnrLineItem lineItem = lineItemBuilder.createLineItem(requisition, nonFullSupplyProducts.get(0));
        lineItemDAO.insertLineItem(lineItem);
      }
    }
  }

  private void createLossesAndAdjustments(RnrLineItem lineItem) {
    for (int i = Integer.valueOf(randomNumeric(1)) - 5; i > 0; i--) {
      LossesAndAdjustmentsType lossesAndAdjustmentsType = adjustmentTypeList.get(i);
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
