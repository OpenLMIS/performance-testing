/*
 * Copyright © 2013 VillageReach. All Rights Reserved. This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 *
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package org.openlmis.performancetesting;

import org.openlmis.core.domain.DosageUnit;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductCategory;
import org.openlmis.core.domain.ProductForm;
import org.openlmis.performancetesting.dao.ProductDAO;
import org.openlmis.performancetesting.helper.ProductBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ProductData {

  private ProductDAO productDAO;
  private ProductBuilder productBuilder = new ProductBuilder();

  public ProductData() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    productDAO = (ProductDAO) ctx.getBean("productDAO");

  }

  public void insertProduct(ProductForm productForm, DosageUnit dosageUnit, ProductCategory category) {
    Product product = productBuilder.createProduct(productForm, dosageUnit, category);
    productDAO.insertProduct(product);

  }

  public List<DosageUnit> setupDosageUnits() {
    List<DosageUnit> dosageUnits = new ArrayList<>();
    String[] codes = {"mg", "ml", "each", "cc", "gm", "mcg", "IU"};

    for (String code : codes) {
      DosageUnit dosageUnit = productBuilder.createDosageUnit(code);
      productDAO.insertDosageUnit(dosageUnit);
      dosageUnits.add(dosageUnit);
    }
    return dosageUnits;
  }

  public List<ProductCategory> setupProductCategories() {
    String[] codes = {"c1", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10"};
    List<ProductCategory> productCategories = new ArrayList<>();
    for (String code : codes) {
      ProductCategory category = productBuilder.createCategory(code);
      productDAO.insertCategory(category);
      productCategories.add(category);
    }
    return productCategories;
  }

  public List<ProductForm> setupProductForms() {
    List<ProductForm> productForms = new ArrayList<>();
    String[] codes = {"Tablet", "Capsule", "Bottle", "Vial", "Ampule", "Drops", "Powder", "Each", "Injecta", "Tube", "Solutio", "Inhaler", "Patch", "Implant", "Sachet", "Device", "Other"};
    for (String code : codes) {
      ProductForm productForm = productBuilder.createForm(code);
      productDAO.insertProductForm(productForm);
      productForms.add(productForm);
    }

    return productForms;
  }
}
