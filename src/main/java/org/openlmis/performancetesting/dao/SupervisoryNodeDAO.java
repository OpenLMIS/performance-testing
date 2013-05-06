/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.core.domain.SupervisoryNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;


@Component
@NoArgsConstructor
public class SupervisoryNodeDAO {

  @Autowired
  NamedParameterJdbcTemplate template;

  final String insertSupervisoryNodeSql = "INSERT INTO supervisory_nodes VALUES(DEFAULT, :parent.id, :facility.id" +
      ", :name, :code, :description, :modifiedBy, :modifiedDate, :modifiedBy, :modifiedDate)";

  public SupervisoryNodeDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }


  public long insertSupervisoryNode(SupervisoryNode supervisoryNode) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(insertSupervisoryNodeSql, new BeanPropertySqlParameterSource(supervisoryNode), keyHolder, new String[]{"id"});

    long id = keyHolder.getKey().longValue();
    supervisoryNode.setId(id);
    return id;
  }

}
