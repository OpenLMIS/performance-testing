/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.Money;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.Program;
import org.openlmis.core.domain.ProgramProduct;

import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.openlmis.performancetesting.DateUtil.randomDate;

public class ProgramProductBuilder {

  public ProgramProduct createProgramProduct(Program program, Product product) {
    ProgramProduct programProduct = new ProgramProduct();
    programProduct.setProgram(program);
    programProduct.setProduct(product);
    programProduct.setActive(true);
    programProduct.setCurrentPrice(new Money("33.3"));
    programProduct.setDosesPerMonth(4);
    programProduct.setModifiedBy(Integer.valueOf(randomNumeric(4)));
    programProduct.setModifiedDate(randomDate());

    return programProduct;
  }
}
