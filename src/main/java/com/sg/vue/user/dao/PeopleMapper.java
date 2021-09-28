package com.sg.vue.user.dao;

import com.sg.vue.user.model.PeopleQueryAO;
import com.sg.vue.user.model.People;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PeopleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(People record);

    int insertSelective(People record);

    People selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(People record);

    List<People> queryUserList();

    Long queryUserListCount(PeopleQueryAO queryAO);
}