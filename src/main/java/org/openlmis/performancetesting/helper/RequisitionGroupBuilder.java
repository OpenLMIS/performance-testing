/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.Facility;
import org.openlmis.core.domain.RequisitionGroup;
import org.openlmis.core.domain.RequisitionGroupMember;
import org.openlmis.core.domain.SupervisoryNode;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.openlmis.performancetesting.DateUtil.randomDate;

public class RequisitionGroupBuilder {

  public RequisitionGroup createRequisitionGroup(SupervisoryNode supervisoryNode) {
    RequisitionGroup requisitionGroup = new RequisitionGroup();
    requisitionGroup.setCode(randomAlphanumeric(30));
    requisitionGroup.setName(randomAlphanumeric(30));
    requisitionGroup.setDescription(randomAlphanumeric(40));
    requisitionGroup.setSupervisoryNode(supervisoryNode);
    requisitionGroup.setModifiedBy(Integer.valueOf(randomNumeric(5)));
    requisitionGroup.setModifiedDate(randomDate());

    return requisitionGroup;
  }

  public RequisitionGroupMember createRequisitionGroupMember(final RequisitionGroup requisitionGroup, final Facility facility) {
    RequisitionGroupMember member = new RequisitionGroupMember();
    member.setFacility(facility);
    member.setRequisitionGroup(requisitionGroup);
    member.setModifiedBy(Integer.valueOf(randomNumeric(5)));
    member.setModifiedDate(randomDate());

    return member;
  }
}
