package com.sg.vue.bootconfig;

import com.sg.vue.cache.CaffeineCache;
import com.sg.vue.converter.ResponseResult;
import com.sg.vue.utils.BootCodes;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BootConfigServiceImpl {

    @Resource
    CaffeineCache caffeineCache;

    @Resource
    BootConfigMapper configMapper;

    public List<BootConfig> queryConfigBuType(BootConfig config) {
        return configMapper.selectConfig(config);
    }

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
}
