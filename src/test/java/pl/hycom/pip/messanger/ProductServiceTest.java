package pl.hycom.pip.messanger;

import lombok.AllArgsConstructor;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.hycom.pip.messanger.model.Product;
import pl.hycom.pip.messanger.service.ProductService;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@AllArgsConstructor
public class ProductServiceTest {

    //@Autowired
    private final ProductService productService;

    @Before
    public void setUp() {
        Product product1 = new Product();
        product1.setId(1);
        product1.setName("name");
        product1.setDescription("desc");
        product1.setImageUrl("url");
        productService.addProduct(product1);
        Product product = new Product();
        product.setId(2);
        product.setName("name");
        product.setDescription("desc");
        product.setImageUrl("url");
        productService.addProduct(product);
    }

    @Test
    public void createProductTest() {
        Product product = new Product();
        product.setId(3);
        product.setName("name");
        product.setDescription("desc");
        product.setImageUrl("url");
        productService.addProduct(product);
        assertEquals(3, productService.findAllProducts().size());
    }

    @Test
    public void findProductByIdTest() {
        assertNotNull(productService.findProductById(1));
        assertEquals("name", productService.findProductById(1).getName());
        assertEquals("desc", productService.findProductById(1).getDescription());
        assertEquals("url", productService.findProductById(1).getImageUrl());
    }

    @Test
    public void deleteProductByIdTest() {
        productService.deleteProduct(2);
        assertEquals(2, productService.findAllProducts().size());
    }

    @Test
    public void updateProductNameTest() {
        productService.updateProductName(1, "zażółć gęślą jaźń");
        assertEquals("zażółć gęślą jaźń", productService.findProductById(1).getName());
    }
}
