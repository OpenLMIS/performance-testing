/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.Program;
import org.openlmis.performancetesting.builder.ProgramBuilder;
import org.openlmis.performancetesting.dao.ProgramDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

public class ProgramData {
  private ProgramDAO programDAO;
  private ProgramBuilder programBuilder;

  public ProgramData() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    programDAO = (ProgramDAO) ctx.getBean("programDAO");
    programBuilder = new ProgramBuilder();

  }

  public ArrayList<Program> setupPrograms() {
    ArrayList<Program> programList = new ArrayList<>();
    String[] programNames = {"ESS MEDICINES", "TB", "MALARIA", "ARV/ART", "VACCINES"};
    for (String programName : programNames) {
      Program program = programBuilder.createProgram(programName);
      programDAO.insertProgram(program);
      programList.add(program);
    }
    return programList;
  }

}
