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
import org.openlmis.performancetesting.dao.ProductDAO;
import org.openlmis.performancetesting.dao.RequisitionDAO;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.domain.RnrLineItem;
import org.openlmis.rnr.domain.RnrStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class RequisitionData {

  private RequisitionBuilder requisitionBuilder;
  private RequisitionDAO requisitionDAO;
  private static final int NUMBER_OF_PERIODS = 12;
  private final LineItemDAO lineItemDAO;
  private final LineItemBuilder lineItemBuilder;
  private final ProductDAO productDAO;

  public RequisitionData() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    requisitionDAO = (RequisitionDAO) ctx.getBean("requisitionDAO");
    lineItemDAO = (LineItemDAO) ctx.getBean("lineItemDAO");
    productDAO = (ProductDAO) ctx.getBean("productDAO");
    requisitionBuilder = new RequisitionBuilder();
    lineItemBuilder = new LineItemBuilder();
  }

  public void createRequisition(List<ProcessingPeriod> periods, Facility facility, Program program,
                                SupervisoryNode supervisoryNode, Facility supplyingFacility, RnrStatus status) {

    for (int periodIndex = 0; periodIndex < NUMBER_OF_PERIODS && periodIndex < periods.size(); periodIndex++) {
      ProcessingPeriod period = periods.get(periodIndex);
      Rnr requisition = requisitionBuilder.createRequisition(facility, program, period, supervisoryNode, supplyingFacility, status);
      requisitionDAO.insertRequisition(requisition);
      createLineItem(requisition, productDAO.getProduct(0), true);
    }
  }

  public void createLineItem(Rnr requisition, Product product, Boolean isFullSupply) {
    RnrLineItem lineItem = lineItemBuilder.createLineItem(requisition, product, isFullSupply);
    lineItemDAO.insertLineItem(lineItem);
  }
}
