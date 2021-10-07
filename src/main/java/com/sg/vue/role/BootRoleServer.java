package com.sg.vue.role;

import com.alibaba.fastjson.JSONObject;
import com.sg.vue.dao.BootRoleMapper;
import com.sg.vue.role.model.BootRoleResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class BootRoleServer {

    @Resource
    BootRoleMapper roleMapper;

    public List<BootRoleResultVO> selectRoleByUserId(Integer userId) {
        if (userId == null) {
            log.error("userId is null, 无法查询权限!!!");
            return null;
        }
        List<BootRoleResultVO> bootRoleResultVOS = roleMapper.selectRoleByUserId(userId);
        log.info("用户：{},权限：{}", userId, JSONObject.toJSONString(bootRoleResultVOS));
        return bootRoleResultVOS;
    }
}
