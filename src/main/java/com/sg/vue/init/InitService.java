package com.sg.vue.init;

import com.sg.vue.bootconfig.BootConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 启动配置文件本地缓存
 */
@Slf4j
@Component
public class InitService implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    BootConfigServiceImpl configService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("init local cache....");
//        configService.reflushAllConfigCache();
    }
}
