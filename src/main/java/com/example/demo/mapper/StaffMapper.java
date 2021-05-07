package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.bean.Staff;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StaffMapper extends BaseMapper<Staff> {

    @Select("select * from staff where id=#{id}")
    public Staff getById(Long id);

    @Insert("INSERT INTO staff ( account, email, password, confirm_code,valid ) " +
            "VALUES ( #{account}, #{email}, #{password}, #{confirmCode}, #{isValid} )")
    int insertStaff(Staff staff);

    @Select("SELECT email FROM staff WHERE confirm_code = #{confirmCode} AND valid = 0")
    Staff selectStaffByConfirmCode(@Param("confirmCode") String confirmCode);

    @Update("UPDATE staff SET valid = 1 WHERE confirm_code = #{confirmCode}")
    int updateStaffByConfirmCode(@Param("confirmCode") String confirmCode);

    @Select("SELECT email, password FROM staff WHERE email = #{email} AND valid =1 ")
    List<Staff> selectStaffByEmail(@Param("email") String email);

    @Select("SELECT account, password FROM staff WHERE account = #{account} AND valid =1 ")
    List<Staff> selectStaffByAccount(@Param("account") String account);
}


