/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.core.domain.RequisitionGroup;
import org.openlmis.core.domain.RequisitionGroupMember;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import static org.apache.commons.lang.RandomStringUtils.randomNumeric;

public class RequisitionGroupDAO {

  NamedParameterJdbcTemplate template;
  final String insertRequisitionGroupQuery = "INSERT INTO requisition_groups VALUES(DEFAULT, :code, :name, :description, :supervisoryNode.id, :modifiedBy, :modifiedDate, %s, :modifiedDate)";
  final String insertRequisitionGroupMemberQuery = "INSERT INTO requisition_group_members VALUES(DEFAULT, :requisitionGroup.id, :facility.id, :modifiedBy, :modifiedDate, %s, :modifiedDate)";

  public RequisitionGroupDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  public int insertRequisitionGroup(RequisitionGroup requisitionGroup) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    String query = String.format(insertRequisitionGroupQuery, randomNumeric(5));
    template.update(query, new BeanPropertySqlParameterSource(requisitionGroup), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    requisitionGroup.setId(id);
    return id;
  }

  public int insertRequisitionMember(RequisitionGroupMember rgMember) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    String query = String.format(insertRequisitionGroupMemberQuery, randomNumeric(5));
    template.update(query, new BeanPropertySqlParameterSource(rgMember), keyHolder, new String[]{"id"});

    int id = keyHolder.getKey().intValue();
    rgMember.setId(id);
    return id;
  }
}
