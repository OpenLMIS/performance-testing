/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.Date;

public class Runner {

  public static final Logger logger = LoggerFactory.getLogger(Runner.class);

  public static void main(String[] args) throws ParseException, InterruptedException {

    final ReferenceData referenceData = new ReferenceData();
    Date startTime = new Date();
    System.out.println("startTime " + startTime);
    referenceData.init();

    referenceData.setupProducts();

    referenceData.insertFacilityAndUsers();

    Date endTime = new Date();
    logger.info("endTime {}", endTime);
    logger.info("time take {}", (endTime.getTime() - startTime.getTime()));
  }


}
