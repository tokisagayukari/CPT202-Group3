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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class StorageController {

    @Autowired
    ProductService productService;

    @Autowired
    StaffService staffService;

    @GetMapping("/storage")
    public String storageList(@RequestParam(value = "pn", defaultValue = "1")Integer pn, Model model){
        List<Product> list = productService.list();
        //model.addAttribute("products", list);
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, null);
        long current = page.getCurrent();
        long pages = page.getPages();
        long total = page.getTotal();
        model.addAttribute("page", page);
        showPortrait(model);
        return "storage_list";
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

    @PostMapping("/storage")
    public String searchStorageName(@RequestParam("keywords") String keywords,
                                    @RequestParam("type") String type,
                                    @RequestParam(value = "priceMin", defaultValue = "0") Integer priceMin,
                                    @RequestParam(value = "priceMax", defaultValue = "-1") Integer priceMax,
                                    @RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                    Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        // wrapper = productService.selectProductsByName(keywords);
        if(!keywords.equals("")) { wrapper.like("name", keywords); }
        if(!type.equals("")) { wrapper.eq("type", type); }
        if(priceMin >= 0) { wrapper.ge("price", priceMin); }
        if(priceMax >= priceMin) { wrapper.le("price", priceMax); }
        List<Product> list = productService.selectProductsByName(keywords);
        // model.addAttribute("products", list);
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "storage_list";
    }

    @GetMapping("/storage/buyingPriceDown")
    public String sortBuyingPriceDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                      Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("price");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "storage_list";
    }

    @GetMapping("/storage/buyingPriceUp")
    public String sortBuyingPriceUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                    Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("price");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "storage_list";
    }

    @GetMapping("/storage/inStoreDown")
    public String sortSalePerWeekDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                      Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("inStore");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "storage_list";
    }

    @GetMapping("/storage/inStoreUp")
    public String sortSalePerWeekUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                    Model model){

        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("inStore");
        IPage<Product> productPage = new Page<>(pn, 18);
        IPage<Product> page = productService.page(productPage, wrapper);
        model.addAttribute("page", page);
        showPortrait(model);
        return "storage_list";
    }
}
