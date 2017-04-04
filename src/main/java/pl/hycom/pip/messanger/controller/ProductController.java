package pl.hycom.pip.messanger.controller;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pl.hycom.pip.messanger.model.Product;
import pl.hycom.pip.messanger.service.ProductService;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Log4j2
public class ProductController {

    private static final String PRODUCTS_VIEW = "products";

    private final ProductService productService;

    @GetMapping("/admin/products")
    public String showProducts(Model model) {
        prepareModel(model, new Product());
        return PRODUCTS_VIEW;
    }

    @PostMapping("/admin/products")
    public String addOrUpdateProduct(@Valid Product product, BindingResult bindingResult, Model model) {
        try {
            if (bindingResult.hasErrors()) {
                prepareModel(model, product);
                model.addAttribute("errors", bindingResult.getFieldErrors());
                log.info("Validation product error !!!");

                return PRODUCTS_VIEW;
            }

            productService.addOrUpdateProduct(product);

        } catch (Exception ex) {
            log.error("Error during post request from admin/products" + ex);
        }

        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/delete")
    public ModelAndView deleteProduct(@RequestParam(value = "productId") final int id) {
        productService.deleteProduct(id);
        log.info("Product[" + id + "] deleted !!!");

        return new ModelAndView("redirect:/admin/products");
    }

    private void prepareModel(Model model, Product product) {
        List<Product> allProducts = productService.findAllProducts();
        model.addAttribute("products", allProducts);
        model.addAttribute("productForm", product);
    }

}
