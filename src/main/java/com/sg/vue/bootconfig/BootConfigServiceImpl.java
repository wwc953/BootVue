package com.sg.vue.bootconfig;

import com.alibaba.fastjson.JSONObject;
import com.sg.vue.cache.CaffeineCache;
import com.sg.vue.converter.ResponseResult;
import com.sg.vue.utils.BootCodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BootConfigServiceImpl {

    @Resource
    CaffeineCache caffeineCache;

    @Resource
    BootConfigMapper configMapper;

    public List<BootConfig> queryConfigBuType(BootConfig config) {
        List<BootConfig> bootConfigs = configMapper.selectConfig(config);
        log.info("queryConfigBuType..{}",JSONObject.toJSONString(bootConfigs));
        return bootConfigs;
    }

    /**
     * 刷新本地缓存
     */
    public void reflushAllConfigCache() {
        caffeineCache.del(BootCodes.config_cach_key);
        BootConfig param = new BootConfig();
        param.setFlag("01");
        List<BootConfig> list = configMapper.selectConfig(param);
        for (BootConfig config : list) {
            caffeineCache.hset(BootCodes.config_cach_key, config.getConfigType(), config.getConfigValue());
        }
    }

    public ResponseResult refreshCache(BootConfig param) {
        if (StringUtils.isBlank(param.getConfigType())) {
            return ResponseResult.fail(BootCodes.errorcode, "type为空");
        }
        param.setFlag("01");
        List<BootConfig> list = configMapper.selectConfig(param);
        if (CollectionUtils.isEmpty(list) || list.size() > 1) {
            return ResponseResult.fail(BootCodes.errorcode, "有效值个数有误!");
        }
        BootConfig config = list.get(0);
        caffeineCache.hset(BootCodes.config_cach_key, config.getConfigType(), config.getConfigValue());
        return ResponseResult.success();
    }

    public Integer saveConfig(BootConfig config) {
        Integer result;
        Date date = new Date();
        if (config.getId() == null) {
            config.setCreateTime(date);
            config.setUpdateTime(date);
            log.info("添加配置:{}", JSONObject.toJSONString(config));
            result = configMapper.insertSelective(config);
        } else {
            log.info("更新配置:{}", JSONObject.toJSONString(config));
            config.setUpdateTime(date);
            result = configMapper.updateByPrimaryKeySelective(config);
        }
        return result;
    }

    public List<Map<String, String>> initConfigTypes() {
        List<Map<String, String>> maps = configMapper.initConfigTypes();
        log.info("initConfigTypes...{}", JSONObject.toJSONString(maps));
        return maps;
    }
}
