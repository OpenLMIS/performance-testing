/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.builder;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.SupervisoryNode;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.openlmis.performancetesting.Utils.randomDate;

public class SupervisoryNodeBuilder {

  public SupervisoryNode createSupervisoryNode(Facility facility, SupervisoryNode parent) {
    SupervisoryNode node = new SupervisoryNode();
    node.setCode(randomAlphanumeric(10));
    node.setName(randomAlphanumeric(30));
    node.setDescription(randomAlphanumeric(50));
    node.setModifiedBy(Long.valueOf(randomNumeric(5)));
    node.setModifiedDate(randomDate());


    if (facility == null) {
      facility = new Facility();
    }
    node.setFacility(facility);

    if (parent == null) {
      parent = new SupervisoryNode();
    }
    node.setParent(parent);

    return node;
  }
}
