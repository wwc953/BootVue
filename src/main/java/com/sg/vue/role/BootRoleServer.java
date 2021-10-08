package com.sg.vue.role;

import com.alibaba.fastjson.JSONObject;
import com.sg.vue.bootconfig.BootConfig;
import com.sg.vue.converter.ResponseResult;
import com.sg.vue.dao.BootConfigMapper;
import com.sg.vue.dao.BootRoleMapper;
import com.sg.vue.role.model.BootRoleResultVO;
import com.sg.vue.role.model.QueryCurrentUserRoleAO;
import com.sg.vue.role.model.SaveUserRoleAO;
import com.sg.vue.utils.BootCodes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BootRoleServer {

    @Resource
    BootRoleMapper roleMapper;

    @Resource
    BootConfigMapper configMapper;

    public List<BootRoleResultVO> selectRoleByUserId(Integer userId) {
        if (userId == null) {
            log.error("userId is null, 无法查询权限!!!");
            return null;
        }
        List<BootRoleResultVO> bootRoleResultVOS = roleMapper.selectRoleByUserId(userId);
        log.info("用户：{},权限：{}", userId, JSONObject.toJSONString(bootRoleResultVOS));
        return bootRoleResultVOS;
    }

    public List<Map> queryAllUrlList() {
        BootConfig config = new BootConfig();
        config.setConfigType("url");
        config.setFlag("01");
        List<BootConfig> bootConfigs = configMapper.selectConfig(config);
        List<Map> resultList = new ArrayList<>();
        bootConfigs.forEach(v -> {
            Map<String, String> result = new HashMap<>();
            result.put("key", String.valueOf(v.getId()));
            result.put("label", v.getValueDes());
            resultList.add(result);
        });
        return resultList;
    }

    public ResponseResult<Set<String>> currentUserRole(QueryCurrentUserRoleAO ao) {
        if (ao.getCurrentUserId() == null) {
            return ResponseResult.fail(BootCodes.errorcode, "userId为空");
        }
        Set<String> resultList = new HashSet<>();
        List<BootRoleResultVO> bootRoleResultVOS = roleMapper.selectRoleByUserId(ao.getCurrentUserId());
        resultList = bootRoleResultVOS.stream().map(BootRoleResultVO::getUrlId).collect(Collectors.toSet());
        log.info("{}用户,所有权限：{}", ao.getCurrentUserId(), JSONObject.toJSONString(resultList));
        return ResponseResult.success(resultList);
    }

    public Integer saveUserRole(SaveUserRoleAO ao) {
        if (ao.getCurrentUserId() == null) {
            throw new RuntimeException("userId为空");
        }
        BootRole del = new BootRole();
        del.setUserId(ao.getCurrentUserId());
        int delete = roleMapper.delete(del);

        for (String urlid : ao.getValue()) {
            BootRole role = new BootRole();
            role.setUserId(ao.getCurrentUserId());
            role.setUrlId(urlid);
            roleMapper.insertSelective(role);
        }

        return 1;
    }
}
