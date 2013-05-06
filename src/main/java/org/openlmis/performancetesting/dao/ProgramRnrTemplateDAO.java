/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import lombok.NoArgsConstructor;
import org.openlmis.rnr.domain.ProgramRnrTemplate;
import org.openlmis.rnr.domain.RnrColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class ProgramRnrTemplateDAO {

  @Autowired
  NamedParameterJdbcTemplate template;

  final String insertRnrTemplateQuery = "INSERT INTO program_rnr_columns VALUES( " +
      "DEFAULT,:id, :programId, :label, :visible, :position, :source.code, TRUE)";

  public void insertRnrTemplate(ProgramRnrTemplate rnrTemplate) {
    for (RnrColumn rnrColumn : rnrTemplate.getRnrColumns()) {
      template.update(insertRnrTemplateQuery, new BeanPropertySqlParameterSource(rnrColumn));
    }
  }

}
