/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.Money;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.System.currentTimeMillis;
import static java.util.Calendar.*;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;

public class Utils {

  public static Date randomDate() {
    return new Date(abs(currentTimeMillis() - Long.valueOf(randomNumeric(11))));
  }

  public static Date periodStartDate(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, 1);
    calendar.set(HOUR_OF_DAY, 0);
    calendar.set(MINUTE, 0);
    calendar.set(SECOND, 0);
    calendar.set(MILLISECOND, 0);

    return calendar.getTime();

  }

  public static Date periodEndDate(int year, int month) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month - 1, 1);
    calendar.set(HOUR_OF_DAY, 0);
    calendar.set(MINUTE, 0);
    calendar.set(SECOND, 0);
    calendar.set(MILLISECOND, 0);
    int numOfDaysInMonth = calendar.getActualMaximum(DAY_OF_MONTH);
    calendar.add(DAY_OF_MONTH, numOfDaysInMonth);
    calendar.set(MILLISECOND, -1);

    return calendar.getTime();

  }

  public static double randomDouble(int min, int max) {
    Random random = new Random();
    return min + (max - min) * random.nextDouble();
  }

  public static Money randomMoney(int length) {
    int min = (int) Math.pow(10, length - 1);
    int max = (int) Math.pow(10, length) - 1;
    Random random = new Random();
    BigDecimal bigDecimal = new BigDecimal(min + (max - min) * random.nextDouble());
    return new Money(bigDecimal);
  }


}
