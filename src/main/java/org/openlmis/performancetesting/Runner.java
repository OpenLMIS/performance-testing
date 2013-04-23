package org.openlmis.performancetesting;

import org.openlmis.core.domain.DosageUnit;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductCategory;
import org.openlmis.core.domain.ProductForm;
import org.openlmis.performancetesting.dao.ProductDAO;
import org.openlmis.performancetesting.helper.ProductHelper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Runner {

  ProductHelper productHelper;
  ProductDAO productDAO;

  public Runner() {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");
    productDAO = (ProductDAO) ctx.getBean("productDAO");

    productHelper = new ProductHelper();


  }


  public static void main(String[] args) {
    Runner runner = new Runner();
    runner.insertData();
  }

  private void insertData() {


    ProductForm productForm = productHelper.createForm();
    productDAO.insertProductForm(productForm);

    DosageUnit dosageUnit = productHelper.createDosageUnit();
    productDAO.insertDosageUnit(dosageUnit);

    ProductCategory category = productHelper.createCategory();
    productDAO.insertCategory(category);

    Product product = productHelper.createProduct(productForm, dosageUnit, category);
    long status = productDAO.insertProduct(product);

    System.out.println(status);

  }

}
