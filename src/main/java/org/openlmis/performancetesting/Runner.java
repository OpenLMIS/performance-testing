package org.openlmis.performancetesting;

import org.openlmis.core.domain.Product;
import org.openlmis.performancetesting.dao.ProductDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Runner {

  public static void main(String[] args) {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-performance.xml");

    ProductDAO dao = (ProductDAO) ctx.getBean("productDAO");
    Product product = createProduct();
    long status = dao.insert(product);
    System.out.println(status);

  }

  private static Product createProduct() {
    Product product = new Product();
    product.setCode("SOME CODE");
    product.setPrimaryName("NAME");
    product.setDispensingUnit("MG");
    product.setDosesPerDispensingUnit(10);
    product.setPackSize(2);
    product.setActive(true);
    product.setFullSupply(false);
    product.setTracer(true);
    product.setRoundToZero(false);
    product.setPackRoundingThreshold(2);
    return product;
  }
}
