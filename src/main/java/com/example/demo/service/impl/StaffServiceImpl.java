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
import java.net.InetAddress;
import java.net.UnknownHostException;
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
        queryWrapper.eq("account", account).eq("password", SecureUtil.md5(password));
        List<Staff> loginUser = staffMapper.selectList(queryWrapper);
        if(loginUser != null){
            return true;
        }
        return false;
    }

    public Map<String,Object> createAccount (Staff staff) throws UnknownHostException {
        String confirmCode = IdUtil.getSnowflake(1,1).nextIdStr();
        String md5_Password = SecureUtil.md5(staff.getPassword());
        staff.setPassword(md5_Password);
        staff.setConfirmCode(confirmCode);
        staff.setIsValid((byte) 0);
        Map<String,Object> resultMap = new HashMap<>();
        if(!staffMapper.selectStaffByAccount(staff.getAccount()).isEmpty()){
            resultMap.put("code",400);
            resultMap.put("msg","Account name already exists!");
        }
        else {
            int result = staffMapper.insertStaff(staff); // result is the number of SQL responding lines


            if (result > 0) {
                String thisIP = InetAddress.getLocalHost().getHostAddress()+":8080";
                String activationUrl = "http://"+thisIP+"/activation?confirmCode="+confirmCode;
                mailService.sendMailForActivation(activationUrl,staff.getEmail());
                resultMap.put("code", 200);
                resultMap.put("msg", "Registry successful, please go to the email to activate your account");

            } else {
                resultMap.put("code", 400);
                resultMap.put("msg", "Registry failed");
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
            resultMap.put("msg", "Activation success");

        } else {
            resultMap.put("code", 400);
            resultMap.put("msg", "Activation failed");
        }
        return resultMap;
    }

    public String forgetPassword (String email, String account, String newPassword, Map<String, Object> map) throws UnknownHostException {
        // Map<String,Object> resultMap = new HashMap<>();
        QueryWrapper<Staff> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email).eq("account", account).eq("valid", (byte) 1);
        if (staffMapper.selectMaps(wrapper).isEmpty()){
            map.put("code",400);
            map.put("msg","No Such Account!");
        }
        else {
            Staff staff = staffMapper.selectOne(wrapper);
            String staffAccount = staff.getAccount();
            staff.setPassword(SecureUtil.md5(newPassword));
            staff.setIsValid((byte) 0);
            String confirmCode = IdUtil.getSnowflake(1,1).nextIdStr();
            staff.setConfirmCode(confirmCode);
            staffMapper.updateById(staff);
            String thisIP = InetAddress.getLocalHost().getHostAddress()+":8080";
            String resetPasswordUrl = "http://"+thisIP+"/activation?confirmCode="+confirmCode;
            mailService.sendMailForResetPassword(resetPasswordUrl,staff.getEmail());
            map.put("code", 200);
            map.put("msg", "Send email successfully");
        }
        return "forget_password";
    }

    public String resetPassword (String account, String password, String newPassword, Map<String, Object> map){
        // Map<String,Object> resultMap = new HashMap<>();
        if (staffMapper.selectStaffByAccount(account).isEmpty()) {
            map.put("code",400);
            map.put("msg","Invalid account");
        }
        else if (!SecureUtil.md5(password).equals(staffMapper.selectStaffByAccount(account).get(0).getPassword())) {
            map.put("code",400);
            map.put("msg","Invalid old password");
        }
        else {
            Staff staff = staffMapper.getByAccount(account);
            staff.setPassword(SecureUtil.md5(newPassword));
            staffMapper.updateById(staff);
            map.put("code", 200);
            map.put("msg", "Reset password successfully");
        }
        return "reset_password";
    }
}
