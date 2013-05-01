/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.builder;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Program;
import org.openlmis.core.domain.SupervisoryNode;
import org.openlmis.core.domain.SupplyLine;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.openlmis.performancetesting.DateUtil.randomDate;

public class SupplyLineBuilder {

  public SupplyLine createSupplyLine(SupervisoryNode supervisoryNode, Program program, Facility facility) {
    SupplyLine supplyLine = new SupplyLine();
    supplyLine.setSupervisoryNode(supervisoryNode);
    supplyLine.setProgram(program);
    supplyLine.setSupplyingFacility(facility);
    supplyLine.setDescription(randomAlphanumeric(50));
    supplyLine.setDescription(randomAlphanumeric(50));
    supplyLine.setModifiedBy(Integer.valueOf(randomNumeric(5)));
    supplyLine.setModifiedDate(randomDate());

    return supplyLine;
  }


}
