/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.builder;

import org.openlmis.core.domain.Product;
import org.openlmis.rnr.domain.LossesAndAdjustments;
import org.openlmis.rnr.domain.LossesAndAdjustmentsType;
import org.openlmis.rnr.domain.Rnr;
import org.openlmis.rnr.domain.RnrLineItem;

import static java.lang.Integer.valueOf;
import static org.apache.commons.lang.RandomStringUtils.*;
import static org.apache.commons.lang.math.RandomUtils.nextBoolean;
import static org.openlmis.performancetesting.Utils.randomDate;
import static org.openlmis.performancetesting.Utils.randomMoney;

public class LineItemBuilder {

  public RnrLineItem createLineItem(Rnr requisition, Product product) {
    RnrLineItem lineItem = new RnrLineItem();

    lineItem.setProductCode(product.getCode());
    lineItem.setRnrId(requisition.getId());
    lineItem.setFullSupply(product.getFullSupply());

    lineItem.setProduct(randomAlphanumeric(100));
    lineItem.setProductDisplayOrder(valueOf(randomNumeric(3)));
    lineItem.setProductCategory(randomAlphabetic(50));
    lineItem.setProductCategoryDisplayOrder(valueOf(randomNumeric(3)));
    lineItem.setDispensingUnit(randomAlphabetic(15));

    Integer beginningBalance = valueOf(randomNumeric(4));
    lineItem.setBeginningBalance(beginningBalance);

    Integer quantityReceived = valueOf(randomNumeric(4));
    lineItem.setQuantityReceived(quantityReceived);
    Integer stockInHand = quantityReceived / 2;
    lineItem.setStockInHand(stockInHand);

    lineItem.setQuantityDispensed(quantityReceived + beginningBalance - stockInHand);


    lineItem.setQuantityRequested(valueOf(randomNumeric(4)));
    lineItem.setReasonForRequestedQuantity(randomAlphabetic(60));
    lineItem.setCalculatedOrderQuantity(valueOf(randomNumeric(4)));
    lineItem.setQuantityApproved(valueOf(randomNumeric(4)));
    lineItem.setTotalLossesAndAdjustments(valueOf(randomNumeric(3)));
    lineItem.setNewPatientCount(valueOf(randomNumeric(3)));
    lineItem.setStockOutDays(valueOf(randomNumeric(2)));
    lineItem.setNormalizedConsumption(valueOf(randomNumeric(2)));
    //TODO fix the AMC datatype
    lineItem.setAmc(valueOf(randomNumeric(2)));
    lineItem.setMaxMonthsOfStock(valueOf(randomNumeric(1)));
    lineItem.setMaxStockQuantity(valueOf(randomNumeric(4)));
    lineItem.setPacksToShip(valueOf(randomNumeric(2)));
    lineItem.setPrice(randomMoney(4));
    lineItem.setRemarks(randomAlphabetic(100));
    lineItem.setDosesPerMonth(valueOf(randomNumeric(2)));
    lineItem.setDosesPerDispensingUnit(valueOf(randomNumeric(3)));
    lineItem.setPackSize(valueOf(randomNumeric(3) + 1));
    lineItem.setRoundToZero(nextBoolean());
    lineItem.setPackRoundingThreshold(valueOf(randomNumeric(2)));
    lineItem.setPreviousStockInHandAvailable(nextBoolean());
    lineItem.setModifiedBy(Long.valueOf(randomNumeric(5)));
    lineItem.setModifiedDate(randomDate());

    return lineItem;

  }

  public LossesAndAdjustments createLossesAndAdjustment(RnrLineItem lineItem, LossesAndAdjustmentsType adjustmentType) {
    LossesAndAdjustments le = new LossesAndAdjustments();
    le.setId(lineItem.getId());
    le.setType(adjustmentType);
    le.setQuantity(valueOf(randomNumeric(2)));
    le.setModifiedBy(Long.valueOf(randomNumeric(3)));
    le.setModifiedDate(randomDate());

    return le;
  }

  public LossesAndAdjustmentsType createAdjustmentType(String name) {
    LossesAndAdjustmentsType type = new LossesAndAdjustmentsType();
    type.setName(name);
    type.setDescription(randomAlphabetic(40));
    type.setAdditive(nextBoolean());
    type.setDisplayOrder(Integer.valueOf(randomNumeric(1)));

    return type;
  }
}
