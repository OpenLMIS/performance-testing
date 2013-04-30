/*
 * Copyright © 2013 VillageReach.  All Rights Reserved.  This Source Code Form is subject to the terms of the Mozilla Public License, v. 20.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.*;

import java.util.Random;

import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang.RandomStringUtils.randomNumeric;
import static org.apache.commons.lang.math.RandomUtils.nextBoolean;
import static org.openlmis.performancetesting.DateUtil.randomDate;

public class ProductBuilder {
  public Product createProduct(ProductForm form, DosageUnit dosageUnit, ProductCategory category) {
    Product product = new Product();
    product.setCode(randomAlphanumeric(40));
    product.setAlternateItemCode(randomAlphanumeric(15));
    product.setManufacturer(randomAlphanumeric(80));
    product.setManufacturerCode(randomAlphanumeric(30));
    product.setManufacturerBarCode(randomAlphanumeric(18));
    product.setMohBarCode(randomAlphanumeric(20));
    product.setGtin(randomAlphanumeric(10));
    product.setType(randomAlphanumeric(60));
    product.setDisplayOrder(Integer.valueOf(randomNumeric(3)));
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
    product.setDosesPerDispensingUnit(Integer.valueOf(randomNumeric(4)));
    product.setPackSize(Integer.valueOf(randomNumeric(4)));
    product.setAlternatePackSize(Integer.valueOf(randomNumeric(4)));
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
    product.setPacksPerCarton(Integer.valueOf(randomNumeric(4)));
    product.setCartonLength(new Random().nextDouble() * 1000);
    product.setCartonWidth(new Random().nextDouble() * 1000);
    product.setCartonHeight(new Random().nextDouble() * 1000);
    product.setCartonsPerPallet(Integer.valueOf(randomNumeric(4)));
    product.setExpectedShelfLife(Integer.valueOf(randomNumeric(4)));
    product.setSpecialStorageInstructions(randomAlphanumeric(100));
    product.setSpecialTransportInstructions(randomAlphanumeric(100));
    product.setActive(nextBoolean());
    product.setFullSupply(nextBoolean());
    product.setTracer(nextBoolean());
    product.setRoundToZero(nextBoolean());
    product.setArchived(nextBoolean());
    product.setPackRoundingThreshold(Integer.valueOf(randomNumeric(3)));
    product.setModifiedBy(Integer.valueOf(randomNumeric(5)));
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
    programProduct.setModifiedBy(Integer.valueOf(randomNumeric(4)));
    programProduct.setModifiedDate(randomDate());

    return programProduct;
  }

  public FacilityApprovedProduct createFacilityApprovedProduct(FacilityType facilityType, ProgramProduct programProduct) {
    FacilityApprovedProduct product = new FacilityApprovedProduct();
    product.setFacilityType(facilityType);
    product.setProgramProduct(programProduct);
    product.setMaxMonthsOfStock(40);
    product.setModifiedBy(Integer.valueOf(randomNumeric(5)));
    product.setModifiedDate(randomDate());

    return product;
  }


  public ProductForm createForm(String code) {
    ProductForm form = new ProductForm();
    form.setCode(code);
    form.setDisplayOrder(Integer.parseInt(randomNumeric(3)));

    return form;
  }

  public DosageUnit createDosageUnit(String code) {
    DosageUnit dosageUnit = new DosageUnit();
    dosageUnit.setCode(code);
    dosageUnit.setDisplayOrder(Integer.parseInt(randomNumeric(3)));

    return dosageUnit;
  }

  public ProductCategory createCategory(String code) {
    ProductCategory category = new ProductCategory();
    category.setCode(code);
    category.setName(randomAlphanumeric(80));
    category.setDisplayOrder(Integer.valueOf(randomNumeric(3)));

    return category;
  }
}
