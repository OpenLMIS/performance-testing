/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.Program;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

public class ProgramHelper {

  public Program createProgram(String code) {
    Program program = new Program();
    program.setCode(code);
    program.setName(randomAlphanumeric(20));
    program.setDescription(randomAlphanumeric(20));
    program.setActive(true);
    program.setTemplateConfigured(true);

    return program;
  }
}
