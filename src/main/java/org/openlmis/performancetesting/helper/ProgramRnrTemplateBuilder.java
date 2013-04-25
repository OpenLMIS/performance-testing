/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.Program;
import org.openlmis.performancetesting.modal.ProgramRnrColumn;
import org.openlmis.rnr.domain.ProgramRnrTemplate;
import org.openlmis.rnr.domain.RnRColumnSource;
import org.openlmis.rnr.domain.RnrColumn;

import java.util.ArrayList;
import java.util.List;

import static org.openlmis.rnr.domain.RnRColumnSource.*;

public class ProgramRnrTemplateBuilder {

  public ProgramRnrTemplate createProgramRnrTemplate(Program program) {
    return new ProgramRnrTemplate(program.getId(), createRnrColumnsList(program.getId()));
  }

  private List<RnrColumn> createRnrColumnsList(final Integer programId) {
    return new ArrayList<RnrColumn>() {{
      add(createRnrColumn(programId, 1, "Product Code", true, 1, REFERENCE));
      add(createRnrColumn(programId, 2, "Product", true, 2, REFERENCE));
      add(createRnrColumn(programId, 3, "Unit/Unit of Issue", true, 3, REFERENCE));
      add(createRnrColumn(programId, 4, "Beginning Balance", true, 4, USER_INPUT));
      add(createRnrColumn(programId, 5, "Total Received Quantity", true, 5, USER_INPUT));
      add(createRnrColumn(programId, 6, "Total Consumed Quantity", true, 6, USER_INPUT));
      add(createRnrColumn(programId, 7, "Total Losses / Adjustments", true, 7, USER_INPUT));
      add(createRnrColumn(programId, 8, "Stock on Hand", true, 8, CALCULATED));
      add(createRnrColumn(programId, 9, "New Patients", true, 9, USER_INPUT));
      add(createRnrColumn(programId, 10, "Total Stockout Days", true, 10, USER_INPUT));
      add(createRnrColumn(programId, 11, "Adjusted Total Consumption", true, 11, CALCULATED));
      add(createRnrColumn(programId, 12, "Average Monthly Consumption(AMC)", true, 12, CALCULATED));
      add(createRnrColumn(programId, 13, "Maximum Stock Quantity", true, 13, CALCULATED));
      add(createRnrColumn(programId, 14, "Calculated Order Quantity", true, 14, CALCULATED));
      add(createRnrColumn(programId, 15, "Requested Quantity", true, 15, USER_INPUT));
      add(createRnrColumn(programId, 16, "Requested Quantity Explanation", true, 16, USER_INPUT));
      add(createRnrColumn(programId, 17, "Approved Quantity", true, 17, USER_INPUT));
      add(createRnrColumn(programId, 18, "Packs to Ship", true, 18, CALCULATED));
      add(createRnrColumn(programId, 19, "Price per Pack", true, 19, REFERENCE));
      add(createRnrColumn(programId, 20, "Total Cost", true, 20, CALCULATED));
      add(createRnrColumn(programId, 21, "Remarks", true, 21, USER_INPUT));
    }};
  }

  private RnrColumn createRnrColumn(Integer programId, Integer masterColumnId, String label, Boolean isVisible, Integer position,
                                    RnRColumnSource source) {
    ProgramRnrColumn rnrColumn = new ProgramRnrColumn();
    rnrColumn.setProgramId(programId);
    rnrColumn.setId(masterColumnId);
    rnrColumn.setLabel(label);
    rnrColumn.setVisible(isVisible);
    rnrColumn.setPosition(position);
    rnrColumn.setSource(source);

    return rnrColumn;

  }
}
