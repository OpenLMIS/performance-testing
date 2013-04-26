/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.ProcessingSchedule;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.openlmis.performancetesting.Randomizer.randomDate;

public class ProcessingScheduleBuilder {

  public ProcessingSchedule createSchedule(String code, String name) {
    ProcessingSchedule schedule = new ProcessingSchedule();
    schedule.setCode(code);
    schedule.setName(name);
    schedule.setDescription(randomAlphanumeric(30));
    schedule.setModifiedBy(Integer.valueOf(randomNumeric(5)));
    schedule.setModifiedDate(randomDate());

    return schedule;
  }

}
