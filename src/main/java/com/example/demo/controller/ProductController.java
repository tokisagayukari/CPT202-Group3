package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.Product;
import com.example.demo.bean.Staff;
import com.example.demo.service.ProductService;

import com.example.demo.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    StaffService staffService;

    @GetMapping("/products")
    public String productsList(@RequestParam(value = "pn", defaultValue = "1")Integer pn, Model model){
        List<Product> list = productService.list();
        //model.addAttribute("products", list);
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, null);
        long current = page.getCurrent();
        long pages = page.getPages();
        long total = page.getTotal();
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    public void showPortrait(Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("account", LoginController.userAccount).eq("password", LoginController.userPassword);
        Map<String, Object> loginUser = staffService.getMap(wrapper);
        String dir;
        if (loginUser.get("portrait") != null) {
            dir = "/face/" + loginUser.get("portrait").toString();
        }
        else {
            dir = "/face/face.jpg";
        }
        model.addAttribute("face", dir);
    }

    @GetMapping("/product")
    public String toProductAddPage(){
        return "product_add";
    }

    @PostMapping("/product")
    public String addProduct(Product product){
        //System.out.println("product:"+product.getOnSale());
        productService.save(product);
        return "redirect:/products";
    }

    @GetMapping("/product/delete/{id}")
    public String removeProduct(@PathVariable("id") Long id,
                                @RequestParam(value = "pn", defaultValue = "1") Integer pn,
                                RedirectAttributes ra){

        productService.removeById(id);
        ra.addAttribute("pn", pn);
        return "redirect:/products";
    }

    @GetMapping("/product/{id}")
    public String toProductEditPage(@PathVariable("id") Integer id,
                                    @RequestParam(value = "pn", defaultValue = "1") Integer pn,
                                    Model model){

        Product product = productService.getById(id);
        model.addAttribute("product", product);
        //ra.addAttribute("pn", pn);
        return "product_add";
    }

    @PutMapping("/product")
    public String editProduct(Product product

                              //@PathVariable("id") Long id,
                              //@RequestParam(value = "pn", defaultValue = "1") Integer pn
                              ){
System.out.println(product);
        productService.updateById(product);
        //ra.addAttribute("pn", pn);
        return "redirect:/products";
    }

    @PostMapping("/products")
    public String searchProductName(@RequestParam("keywords") String keywords,
                                    @RequestParam("type") String type,
                                    @RequestParam(value = "priceMin", defaultValue = "0") Integer priceMin,
                                    @RequestParam(value = "priceMax", defaultValue = "-1") Integer priceMax,
                                    @RequestParam(value = "onSaleMin", defaultValue = "0") Integer onSaleMin,
                                    @RequestParam(value = "onSaleMax", defaultValue = "-1") Integer onSaleMax,
                                    @RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                    Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        // wrapper = productService.selectProductsByName(keywords);
        if(!keywords.equals("")) { wrapper.like("name", keywords); }
        if(!type.equals("")) { wrapper.eq("type", type); }
        if(priceMin >= 0) { wrapper.ge("price", priceMin); }
        if(priceMax >= priceMin) { wrapper.le("price", priceMax); }
        if(onSaleMin >= 0) { wrapper.ge("on_sale", onSaleMin); }
        if(onSaleMax >= onSaleMin) { wrapper.le("on_sale", onSaleMax); }
        List<Product> list = productService.selectProductsByName(keywords);
        // model.addAttribute("products", list);
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/priceDown")
    public String sortPriceDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("price");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/priceUp")
    public String sortPriceUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("price");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/discountDown")
    public String sortdiscountDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("discount");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/discountUp")
    public String sortdiscountUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                              Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("discount");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/onSaleDown")
    public String sortOnSaleDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("onSale");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/onSaleUp")
    public String sortOnSaleUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                              Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("onSale");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/inStoreDown")
    public String sortInStoreDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("inStore");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/inStoreUp")
    public String sortInStoreUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                              Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("inStore");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/salePerWeekDown")
    public String sortSalePerWeekDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("salePerWeek");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }

    @GetMapping("/products/salePerWeekUp")
    public String sortSalePerWeekUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                              Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("salePerWeek");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "product_list";
    }
}
