/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.builder;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Program;
import org.openlmis.core.domain.ProgramSupported;

import static org.openlmis.performancetesting.Utils.periodStartDate;

public class ProgramsSupportedBuilder {
  public ProgramSupported createProgramsSupported(Facility facility, Program program) {
    ProgramSupported programSupported = new ProgramSupported();
    programSupported.setFacilityId(facility.getId());
    programSupported.setActive(true);
    programSupported.setProgram(program);
    programSupported.setStartDate(periodStartDate(2010, 1));

    return programSupported;
  }


}
