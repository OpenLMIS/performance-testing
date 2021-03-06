/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.RequisitionGroup;
import org.openlmis.core.domain.RequisitionGroupMember;
import org.openlmis.core.domain.RequisitionGroupProgramSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class RequisitionGroupDAO {

  @Autowired
  NamedParameterJdbcTemplate template;

  final String insertRequisitionGroupQuery = "INSERT INTO requisition_groups VALUES(DEFAULT, :code, :name, :description, :supervisoryNode.id, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  final String insertRequisitionGroupMemberQuery = "INSERT INTO requisition_group_members VALUES(DEFAULT, :requisitionGroup.id, :facility.id, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  final String insertRequisitionGroupProgramScheduleQuery = "INSERT INTO requisition_group_program_schedules " +
      "VALUES(DEFAULT, :requisitionGroup.id,  :program.id, :processingSchedule.id, :directDelivery, :dropOffFacility.id," +
      ":modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  public long insertRequisitionGroup(RequisitionGroup requisitionGroup) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertRequisitionGroupQuery, new BeanPropertySqlParameterSource(requisitionGroup), keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    requisitionGroup.setId(id);
    return id;
  }

  public long insertRequisitionMember(RequisitionGroupMember rgMember) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertRequisitionGroupMemberQuery, new BeanPropertySqlParameterSource(rgMember), keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    rgMember.setId(id);
    return id;
  }

  public long insertRequisitionGroupProgramSchedule(RequisitionGroupProgramSchedule requisitionGroupProgramSchedule) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertRequisitionGroupProgramScheduleQuery, new BeanPropertySqlParameterSource(requisitionGroupProgramSchedule), keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    requisitionGroupProgramSchedule.setId(id);
    return id;
  }
}
