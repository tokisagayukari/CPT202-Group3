package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.Product;
import com.example.demo.bean.Staff;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
public class StaffController {

    @Autowired
    StaffService staffService;

    @GetMapping("/staffs")
    public String staffsList(@RequestParam(value = "pn", defaultValue = "1")Integer pn, Model model){
        List<Staff> list = staffService.list();
        //model.addAttribute("staffs", list);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, null);
        long current = page.getCurrent();
        long pages = page.getPages();
        long total = page.getTotal();
        model.addAttribute("page", page);
        return "staff_list";
    }

    @GetMapping("/staff")
    public String toStaffAddPage(){
        return "Staff_add";
    }

    @PostMapping("/staff")
    public String addStaff(Staff staff){
        staffService.save(staff);
        return "redirect:/staffs";
    }

    @GetMapping("/staff/delete/{id}")
    public String removeStaff(@PathVariable("id") Long id,
                              @RequestParam(value = "pn", defaultValue = "1") Integer pn,
                              RedirectAttributes ra){

        staffService.removeById(id);
        ra.addAttribute("pn", pn);
        return "redirect:/staffs";
    }

    @GetMapping("/staff/{id}")
    public String toStaffEditPage(@PathVariable("id") Integer id,
                                  @RequestParam(value = "pn", defaultValue = "1") Integer pn,
                                  Model model){

        Staff staff = staffService.getById(id);
        model.addAttribute("staff", staff);
        //ra.addAttribute("pn", pn);
        return "staff_add";
    }

    @PutMapping("/staff")
    public String editStaff(Staff staff

                            //@PathVariable("id") Long id,
                            //@RequestParam(value = "pn", defaultValue = "1") Integer pn
                            ){

        staffService.updateById(staff);
        //ra.addAttribute("pn", pn);
        return "redirect:/staffs";
    }

    @PostMapping("/staffs")
    public String searchStaffName(@RequestParam("keywords") String keywords,
                                  @RequestParam("position") String position,
                                  @RequestParam("account") String account,
                                  @RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                  Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        // wrapper = staffService.selectStaffsByName(keywords);
        if(!keywords.equals("")) { wrapper.like("name", keywords); }
        if(!position.equals("")) { wrapper.eq("position", position); }
        if(!account.equals("")) { wrapper.like("account", account); }
        // List<Staff> list = staffService.selectStaffsByName(keywords);
        // model.addAttribute("staffs", list);
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        return "staff_list";
    }

    @GetMapping("/staffs/nameDown")
    public String sortNameDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                               Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("name");
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        return "staff_list";
    }
    @GetMapping("/staffs/nameUp")
    public String sortNameUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                             Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("name");
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        return "staff_list";
    }


    @GetMapping("/staffs/accountDown")
    public String sortAccountDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                  Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("account");
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        return "staff_list";
    }

    @GetMapping("/staffs/accountUp")
    public String sortAccountUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("account");
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        return "staff_list";
    }

    @GetMapping("/staffs/positionDown")
    public String sortOnSaleDown(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                                 Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("position");
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        return "staff_list";
    }

    @GetMapping("/staffs/positionUp")
    public String sortOnSaleUp(@RequestParam(value = "pn", defaultValue = "1")Integer pn,
                               Model model){

        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("position");
        IPage<Staff> staffPage = new Page<>(pn, 18);
        IPage<Staff> page = staffService.page(staffPage, wrapper);
        model.addAttribute("page", page);
        return "staff_list";
    }
}
