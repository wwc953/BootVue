package com.sg.vue.bootconfig;

import com.sg.vue.cache.CaffeineCache;
import com.sg.vue.utils.BootCodes;
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
//            caffeineCache.hdel(BootCodes.config_cach_key, config.getConfigType());
            caffeineCache.hset(BootCodes.config_cach_key, config.getConfigType(), config.getConfigValue());
        }
    }
}
