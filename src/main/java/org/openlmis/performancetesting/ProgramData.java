/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.Program;
import org.openlmis.core.domain.ProgramSupported;
import org.openlmis.performancetesting.builder.ProgramBuilder;
import org.openlmis.performancetesting.builder.ProgramsSupportedBuilder;
import org.openlmis.performancetesting.dao.ProgramDAO;
import org.openlmis.performancetesting.dao.ProgramsSupportedDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.openlmis.performancetesting.Constants.*;

public class ProgramData {

  private ProgramDAO programDAO;
  private ProgramBuilder programBuilder;

  private ProgramsSupportedDAO programsSupportedDAO;
  private ProgramsSupportedBuilder programSupportedBuilder;

  public ProgramData() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    programDAO = (ProgramDAO) ctx.getBean("programDAO");
    programsSupportedDAO = (ProgramsSupportedDAO) ctx.getBean("programsSupportedDAO");
    programBuilder = new ProgramBuilder();
    programSupportedBuilder = new ProgramsSupportedBuilder();

  }

  public ArrayList<Program> setupPrograms(Map<Program, List<Integer>> programProductCountMap) {
    ArrayList<Program> programList = new ArrayList<>();
    String[] programNames = {"ESS MEDICINES", "TB", "MALARIA", "ARV/ART", "VACCINES"};
    for (String programName : programNames) {
      Program program = programBuilder.createProgram(programName);
      programDAO.insertProgram(program);
      programList.add(program);
      if (programName.equals("ESS MEDICINES")) {
        programProductCountMap.put(program, asList(MAX_FULL_SUPPLY_COUNT, NON_FULL_SUPPLY_COUNT));
      } else {
        programProductCountMap.put(program, asList(AVERAGE_FULL_SUPPLY_COUNT, NON_FULL_SUPPLY_COUNT));
      }

    }
    return programList;
  }

  public ProgramSupported setupProgramSupported(Program program, Facility facility) {
    ProgramSupported programsSupported = programSupportedBuilder.createProgramsSupported(facility, program);
    programsSupportedDAO.insertProgramSupported(programsSupported);

    return programsSupported;
  }
}
