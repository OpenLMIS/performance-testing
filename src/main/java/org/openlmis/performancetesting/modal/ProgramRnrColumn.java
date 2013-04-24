/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.modal;

import lombok.Getter;
import lombok.Setter;
import org.openlmis.rnr.domain.RnrColumn;

public class ProgramRnrColumn extends RnrColumn {

  @Getter
  @Setter
  private Integer programId;

}
