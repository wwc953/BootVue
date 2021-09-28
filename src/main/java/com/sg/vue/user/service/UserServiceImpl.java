package com.sg.vue.user.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sg.vue.user.model.PeopleQueryAO;
import com.sg.vue.user.model.People;
import com.sg.vue.user.dao.PeopleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl {

    @Resource
    PeopleMapper peopleMapper;

    public List<People> queryUserList(PeopleQueryAO queryAO) {
        Page<Object> page = PageHelper.startPage(queryAO.getPageNo(), queryAO.getPageSize());
        return peopleMapper.queryUserList();
    }

    public Long queryUserListCount(PeopleQueryAO queryAO) {
        long count = peopleMapper.queryUserListCount(queryAO);
        return count;
    }


    public Integer saveUser(People people) {
        Integer result;
        people.setUpdateTime(new Date());
        if (people.getId() == null) {
            log.info("addUser...{}", JSON.toJSONString(people));
            result = peopleMapper.insertSelective(people);
        } else {
            log.info("updateUser...{}", JSON.toJSONString(people));
            result = peopleMapper.updateByPrimaryKeySelective(people);
        }
        return result;
    }

    public Integer delUser(People people) {
        if (people.getId() == null) {
            throw new RuntimeException("id is null");
        }
        People del = new People();
        del.setId(people.getId());
        del.setFlag("02");
        del.setUpdateTime(new Date());
        log.info("delUser...{}", JSON.toJSONString(del));
        return peopleMapper.updateByPrimaryKeySelective(del);
    }
}
