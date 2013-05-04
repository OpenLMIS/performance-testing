/*
 * Copyright © 2013 VillageReach.  All Rights Reserved.  This Source Code Form is subject to the terms of the Mozilla Public License, v. 20.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.builder;

import org.openlmis.core.domain.*;

import java.util.Random;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang.math.RandomUtils.nextBoolean;
import static org.openlmis.performancetesting.Utils.randomDate;

public class ProductBuilder {
  public Product createProduct(ProductForm form, DosageUnit dosageUnit, ProductCategory category, Boolean isFulLSupply) {
    Product product = new Product();
    product.setCode(randomAlphanumeric(40));
    product.setAlternateItemCode(randomAlphanumeric(15));
    product.setManufacturer(randomAlphanumeric(80));
    product.setManufacturerCode(randomAlphanumeric(30));
    product.setManufacturerBarCode(randomAlphanumeric(18));
    product.setMohBarCode(randomAlphanumeric(20));
    product.setGtin(randomAlphanumeric(10));
    product.setType(randomAlphanumeric(60));
    product.setDisplayOrder(valueOf(randomNumeric(3)));
    product.setPrimaryName(randomAlphanumeric(120));
    product.setFullName(randomAlphanumeric(200));
    product.setGenericName(randomAlphanumeric(80));
    product.setAlternateName(randomAlphanumeric(60));
    product.setDescription(randomAlphanumeric(100));
    product.setStrength(randomAlphanumeric(13));

    product.setForm(form);
    product.setDosageUnit(dosageUnit);
    product.setCategory(category);

    product.setDispensingUnit(randomAlphanumeric(20));
    product.setDosesPerDispensingUnit(valueOf(randomNumeric(4)));
    product.setPackSize(valueOf(randomNumeric(4)));
    product.setAlternatePackSize(valueOf(randomNumeric(4)));
    product.setStoreRefrigerated(nextBoolean());
    product.setStoreRoomTemperature(nextBoolean());
    product.setHazardous(nextBoolean());
    product.setFlammable(nextBoolean());
    product.setControlledSubstance(nextBoolean());
    product.setLightSensitive(nextBoolean());
    product.setApprovedByWHO(nextBoolean());
    product.setContraceptiveCYP(new Random().nextDouble() * 1000);
    product.setPackLength(new Random().nextDouble() * 1000);
    product.setPackWidth(new Random().nextDouble() * 1000);
    product.setPackHeight(new Random().nextDouble() * 1000);
    product.setPackWeight(new Random().nextDouble() * 1000);
    product.setPacksPerCarton(valueOf(randomNumeric(4)));
    product.setCartonLength(new Random().nextDouble() * 1000);
    product.setCartonWidth(new Random().nextDouble() * 1000);
    product.setCartonHeight(new Random().nextDouble() * 1000);
    product.setCartonsPerPallet(valueOf(randomNumeric(4)));
    product.setExpectedShelfLife(valueOf(randomNumeric(4)));
    product.setSpecialStorageInstructions(randomAlphanumeric(100));
    product.setSpecialTransportInstructions(randomAlphanumeric(100));
    product.setActive(true);
    product.setFullSupply(isFulLSupply);
    product.setTracer(nextBoolean());
    product.setRoundToZero(nextBoolean());
    product.setArchived(nextBoolean());
    product.setPackRoundingThreshold(valueOf(randomNumeric(3)));
    product.setModifiedBy(Long.valueOf(randomNumeric(5)));
    product.setModifiedDate(randomDate());

    return product;
  }

  public ProgramProduct createProgramProduct(Program program, Product product) {
    ProgramProduct programProduct = new ProgramProduct();
    programProduct.setProgram(program);
    programProduct.setProduct(product);
    programProduct.setActive(true);
    programProduct.setCurrentPrice(new Money("33.3"));
    programProduct.setDosesPerMonth(4);
    programProduct.setModifiedBy(Long.valueOf(randomNumeric(4)));
    programProduct.setModifiedDate(randomDate());

    return programProduct;
  }

  public FacilityApprovedProduct createFacilityApprovedProduct(FacilityType facilityType, ProgramProduct programProduct) {
    FacilityApprovedProduct product = new FacilityApprovedProduct();
    product.setFacilityType(facilityType);
    product.setProgramProduct(programProduct);
    product.setMaxMonthsOfStock(valueOf(randomNumeric(2)));
    product.setModifiedBy(Long.valueOf(randomNumeric(5)));
    product.setModifiedDate(randomDate());

    return product;
  }


  public ProductForm createForm(String code) {
    ProductForm form = new ProductForm();
    form.setCode(code);
    form.setDisplayOrder(parseInt(randomNumeric(3)));

    return form;
  }

  public DosageUnit createDosageUnit(String code) {
    DosageUnit dosageUnit = new DosageUnit();
    dosageUnit.setCode(code);
    dosageUnit.setDisplayOrder(parseInt(randomNumeric(3)));

    return dosageUnit;
  }

  public ProductCategory createCategory(String code) {
    ProductCategory category = new ProductCategory();
    category.setCode(code);
    category.setName(randomAlphanumeric(80));
    category.setDisplayOrder(valueOf(randomNumeric(3)));

    return category;
  }
}
