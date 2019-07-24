package com.example.seckill.dao;

import com.example.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {


    @Select("select * from user where id = #{id}")
    User getById(@Param("id") int id);

    //测试注释
    @Insert("insert into user (id,name) values(#{id},#{name})")
    void insertUser(User user);

}
