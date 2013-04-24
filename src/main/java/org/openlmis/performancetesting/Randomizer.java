/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import java.util.Date;

import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;

public class Randomizer {

  public static Date randomDate() {
    return new Date(abs(currentTimeMillis() - Long.valueOf(randomNumeric(11))));
  }

}
