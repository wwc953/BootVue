package com.sg.vue.dao;

import com.sg.vue.bootconfig.BootConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BootConfigMapper {

    int insertSelective(BootConfig record);

    List<BootConfig> selectConfig(BootConfig record);

    int updateByPrimaryKeySelective(BootConfig record);

    List<Map<String, String>> initConfigTypes();
}