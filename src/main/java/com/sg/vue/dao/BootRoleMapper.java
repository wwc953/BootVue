package com.sg.vue.dao;

import com.sg.vue.role.BootRole;
import com.sg.vue.role.model.BootRoleResultVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BootRoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insertSelective(BootRole record);

    BootRole selectByPrimaryKey(Integer roleId);

    /**
     * 根据userId查询该用户菜单权限
     * @param userId
     * @return
     */
    List<BootRoleResultVO> selectRoleByUserId(Integer userId);

    int updateByPrimaryKeySelective(BootRole record);

}