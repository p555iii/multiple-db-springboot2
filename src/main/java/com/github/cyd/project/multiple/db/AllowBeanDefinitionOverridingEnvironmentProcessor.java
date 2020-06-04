package com.github.cyd.project.multiple.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description  允许覆盖bean  beanName相同 以后者为准
 *               参见
 *                org.springframework.cloud.client.HostInfoEnvironmentPostProcessor
 * @Author changyandong
 * @Emoji (゜ - ゜)つ干杯
 * @Created Date: 2019/8/28 17:01
 * @ClassName AllowBeanDefinitionOverridingEnvironmentProcessor
 * @Version: 1.0
 */
public class AllowBeanDefinitionOverridingEnvironmentProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> paramMap = new HashMap<>(4);
        paramMap.put("spring.main.allow-bean-definition-overriding","true");
        paramMap.put("spring.jpa.hibernate.naming.physical-strategy","com.github.cyd.project.common.db.PhysicalNamingStrategy");
        MapPropertySource mapPropertySource = new MapPropertySource("allowBeanDefinitionOverridingPropSource", paramMap);
        environment.getPropertySources().addLast(mapPropertySource);
    }
}
