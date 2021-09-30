package com.sg.vue.bootconfig;

import com.sg.vue.converter.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/config")
public class BootConfigController {

    @Resource
    BootConfigServiceImpl configService;

    @PostMapping("/queryConfigByType")
    public ResponseResult queryConfigBuType(@RequestBody BootConfig config) {
        return ResponseResult.success(configService.queryConfigBuType(config));
    }

    @PostMapping("/saveConfig")
    public ResponseResult saveConfig(@RequestBody BootConfig config) {
        return ResponseResult.success(configService.saveConfig(config));
    }

    @PostMapping("/refreshCache")
    public ResponseResult refreshCache(@RequestBody BootConfig config) {
        return  configService.refreshCache(config) ;
    }
}
