package com.sg.vue.dao;

import com.sg.vue.user.model.PeopleQueryAO;
import com.sg.vue.user.model.People;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PeopleMapper {
    int insertSelective(People record);

    People queryPeople(People people);

    int updateByPrimaryKeySelective(People record);

    List<People> queryUserList();

    Long queryUserListCount(PeopleQueryAO queryAO);
}