package org.openlmis.performancetesting.helper;

import org.openlmis.core.domain.DosageUnit;
import org.openlmis.core.domain.Product;
import org.openlmis.core.domain.ProductCategory;
import org.openlmis.core.domain.ProductForm;

public class ProductHelper {
  public Product createProduct(ProductForm form, DosageUnit dosageUnit, ProductCategory category) {
    Product product = new Product();
    product.setCode("SOME CODE ");
    product.setAlternateItemCode("alternative");
    product.setManufacturer("manu");
    product.setManufacturerCode("manu code");
    product.setManufacturerBarCode("bar");
    product.setMohBarCode("mohbar");
    product.setGtin("gtin");
    product.setType("tty");
    product.setDisplayOrder(999);
    product.setPrimaryName("pname");
    product.setFullName("full");
    product.setGenericName("generic");
    product.setAlternateName("aname");
    product.setDescription("desc");
    product.setStrength("str");

    product.setForm(form);
    product.setDosageUnit(dosageUnit);
    product.setCategory(category);

    product.setDispensingUnit("disp-unit");
    product.setDosesPerDispensingUnit(-1);
    product.setPackSize(2323);
    product.setAlternatePackSize(3232);
    product.setStoreRefrigerated(false);
    product.setStoreRoomTemperature(true);
    product.setHazardous(false);
    product.setFlammable(true);
    product.setControlledSubstance(false);
    product.setLightSensitive(true);
    product.setApprovedByWHO(false);
    product.setContraceptiveCYP(0.1);
    product.setPackLength(1.1);
    product.setPackWidth(2.2);
    product.setPackHeight(3.3);
    product.setPackWeight(4.4);
    product.setPacksPerCarton(5);
    product.setCartonLength(6.6);
    product.setCartonWidth(7.7);
    product.setCartonHeight(8.8);
    product.setCartonsPerPallet(9);
    product.setExpectedShelfLife(10);
    product.setSpecialStorageInstructions("sp-store");
    product.setSpecialTransportInstructions("sp-trf");
    product.setActive(true);
    product.setFullSupply(false);
    product.setTracer(true);
    product.setRoundToZero(false);
    product.setArchived(true);
    product.setPackRoundingThreshold(11);
    product.setModifiedBy(12);

    return product;
  }

  public ProductForm createForm() {
    ProductForm form = new ProductForm();
    form.setCode("SOME FORM CODE");
    form.setDisplayOrder(123);
    return form;
  }

  public DosageUnit createDosageUnit() {
    DosageUnit dosageUnit = new DosageUnit();
    dosageUnit.setCode("ABCD");
    dosageUnit.setDisplayOrder(932);
    return dosageUnit;
  }

  public ProductCategory createCategory() {
    ProductCategory category = new ProductCategory();
    category.setCode("CATEGORY ABC123CYZ CATEGORY-CATEGORY");
    category.setDisplayOrder(435);
    category.setName("CATEGORY NAMEEEEEEEEWERWERWW");
    return category;
  }
}
