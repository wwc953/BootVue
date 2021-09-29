package com.sg.vue.init;

import com.sg.vue.bootconfig.BootConfigServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class InitService implements ApplicationListener<ApplicationStartedEvent> {

    @Resource
    BootConfigServiceImpl configService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        configService.reflushAllConfigCache();
    }
}
