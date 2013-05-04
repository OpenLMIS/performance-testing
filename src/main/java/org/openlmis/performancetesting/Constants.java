/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

public class Constants {

  public static final int STATES_PER_COUNTRY = 35; // 35
  public static final int DISTRICT_PER_STATE = 25; // 25
  public static final int FACILITIES_PER_DISTRICT = 28; // 28
  public static final int FACILITIES_PER_REQUISITION_GROUP = 100; // 100
  public static final int NO_OF_FACILITY_TYPES = 5;
  public static final int NUMBER_OF_PERIODS = 12;
  public static final int STARTING_PERIOD_MONTH = 10;


  public static final String[] PROGRAM_NAMES = {"ESS MEDICINES", "TB", "MALARIA", "ARV/ART", "VACCINES"};

  public static final int MAX_FULL_SUPPLY_COUNT = 400;     //400
  public static final int NON_FULL_SUPPLY_COUNT = 100;     //100
  public static final int AVERAGE_FULL_SUPPLY_COUNT = 35;  //35
  public static final int TOTAL_NO_OF_PRODUCTS = 2000;      //2000

  public static final String[] ADJUSTMENT_NAMES = {"TRANSFER_IN", "TRANSFER_OUT", "DAMAGED", "LOST", "STOLEN", "EXPIRED", "PASSED_OPEN_VIAL_TIME_LIMIT", "COLD_CHAIN_FAILURE", "CLINIC_RETURN"};

  public static final int NUMBER_OF_NON_FULL_SUPPLY_ITEMS = 10;
}