/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.dao;

import org.openlmis.rnr.domain.ProgramRnrTemplate;
import org.openlmis.rnr.domain.RnrColumn;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class ProgramRnrTemplateDAO {
  NamedParameterJdbcTemplate template;

  String insertRnrTemplateQuery = "INSERT INTO program_rnr_columns VALUES( " +
      "DEFAULT,:id, :programId, :label, :visible, :position, :source.code, TRUE)";

  public ProgramRnrTemplateDAO(NamedParameterJdbcTemplate template) {
    this.template = template;
  }

  public void insertRnrTemplate(ProgramRnrTemplate rnrTemplate) {
    for (RnrColumn rnrColumn : rnrTemplate.getRnrColumns()) {
      template.update(insertRnrTemplateQuery, new BeanPropertySqlParameterSource(rnrColumn));
    }
  }

}
