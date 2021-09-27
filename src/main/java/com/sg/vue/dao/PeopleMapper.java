package com.sg.vue.dao;

import com.sg.vue.model.ao.PeopleQueryAO;
import com.sg.vue.model.dto.People;

import java.util.List;

public interface PeopleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(People record);

    int insertSelective(People record);

    People selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(People record);

    List<People> queryUserList();

    Long queryUserListCount(PeopleQueryAO queryAO);
}