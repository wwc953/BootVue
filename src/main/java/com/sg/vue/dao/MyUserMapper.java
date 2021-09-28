package com.sg.vue.dao;

import com.sg.vue.model.dto.MyUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MyUser record);

    int insertSelective(MyUser record);

    MyUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MyUser record);

    int updateByPrimaryKey(MyUser record);
}