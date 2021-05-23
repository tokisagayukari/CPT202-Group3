package com.example.demo.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.bean.Staff;
import com.example.demo.mapper.StaffMapper;
import com.example.demo.service.MailService;
import com.example.demo.service.StaffService;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StaffServiceImpl extends ServiceImpl<StaffMapper, Staff> implements StaffService {

    @Autowired
    StaffMapper staffMapper;

    @Resource
    private MailService mailService;

    @Update("ALTER TABLE `staff` AUTO_INCREMENT=1;")
    public void resetAutoIncrement() {}

    public boolean checkAccountAndPassword(String account, String password){

        QueryWrapper<Staff> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account).eq("password", password);
        List<Staff> loginUser = staffMapper.selectList(queryWrapper);
        if(loginUser != null){
            return true;
        }
        return false;
    }

    public Map<String,Object> createAccount (Staff staff){
        String confirmCode = IdUtil.getSnowflake(1,1).nextIdStr();
        String md5_Password = SecureUtil.md5(staff.getPassword());
        staff.setPassword(md5_Password);
        staff.setConfirmCode(confirmCode);
        staff.setIsValid((byte) 0);
        Map<String,Object> resultMap = new HashMap<>();
        if(!staffMapper.selectStaffByAccount(staff.getAccount()).isEmpty()){
            resultMap.put("code",400);
            resultMap.put("msg","account name already exists!");
        }
        else {
            int result = staffMapper.insertStaff(staff); // result is the number of SQL responding lines


            if (result > 0) {
                String activationUrl = "http://localhost:8080/activation?confirmCode="+confirmCode;
                mailService.sendMailForActivation(activationUrl,staff.getEmail());
                resultMap.put("code", 200);
                resultMap.put("msg", "registry successful, please go to the email to activate your account");

            } else {
                resultMap.put("code", 400);
                resultMap.put("msg", "registry failed");
            }
        }
        return resultMap;
    }

    public Map<String, Object> activationAccount(String confirmCode) {
        Map<String, Object> resultMap = new HashMap<>();
        Staff staff = staffMapper.selectStaffByConfirmCode(confirmCode);
        int result = staffMapper.updateStaffByConfirmCode(confirmCode);
        if (result > 0) {
            resultMap.put("code", 200);
            resultMap.put("msg", "activation success");

        } else {
            resultMap.put("code", 400);
            resultMap.put("msg", "activation failed");
        }
        return resultMap;
    }

    public Map<String,Object> forgetPassword (String email){
        Staff staff = staffMapper.getByEmail(email);
        String staffAccount=staff.getAccount();
        staff.setIsValid((byte) 0);
        Map<String,Object> resultMap = new HashMap<>();
        if(staffMapper.selectStaffByAccount(staff.getAccount()).isEmpty()){
            resultMap.put("code",400);
            resultMap.put("msg","No Such Account!");
        }
        else {
            String resetPasswordUrl = "http://localhost:8080/resetPassword?"+staffAccount;
            mailService.sendMailForResetPassword(resetPasswordUrl,staff.getEmail());
            resultMap.put("code", 200);
            resultMap.put("msg", "Send email successfully");
        }
        return resultMap;
    }

    public Map<String,Object> resetPassword (String account,String password){
        Map<String,Object> resultMap = new HashMap<>();
        if(staffMapper.selectStaffByAccount(account).isEmpty()){
            resultMap.put("code",400);
            resultMap.put("msg","No Such Account!");
        }
        else {
            Staff staff = staffMapper.getByAccount(account);
            staff.setPassword(password);
            resultMap.put("code", 200);
            resultMap.put("msg", "ResetPassword successfully");
            staffMapper.updateById(staff);

        }
        return resultMap;
    }
}
