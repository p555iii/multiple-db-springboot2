package com.github.cyd.project.multiple.db;

import org.mybatis.spring.annotation.MapperScannerRegistrar;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @Description  Mybatis 扫描 注册
 * @Author changyandong
 * @Created Date: 2018/7/27 10:56
 * @ClassName MapperScannerRegistrar
 * @Version: 1.0
 */
public class CustomMapperScannerRegistrar extends MapperScannerRegistrar {
    private ResourceLoader resourceLoader;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //因为@Import以后的这个方法执行太早，配置文件还无法加载。所以使用函数式编程，将该方法注册到需要使用的地方执行，那时配置文件会加载到
        //那个方法，通过静态变量拿到配置值后再执行这个方法。
        try {
            Consumer<BeanDefinitionRegistry> userConsumer = mybatisRegistry -> {
                for (Map.Entry<String, Map<String,String>> entry : DynamicDataSourceConfiguration.datasource.entrySet()) {
                    String dataSourceName = entry.getKey();
                    Map<String,String> prop = entry.getValue();
                    if(Objects.equals(prop.get(Constant.MYBATIS_USE),Boolean.FALSE.toString())){
                        continue;
                    }
                    DynamicDataSourceConfiguration.setDefaultMybatisScanAndPackage(dataSourceName,prop);
                    ClassPathMapperScanner scanner = new ClassPathMapperScanner(mybatisRegistry);
                    if (this.resourceLoader != null) {
                        scanner.setResourceLoader(this.resourceLoader);
                    }
                    scanner.setSqlSessionTemplateBeanName(dataSourceName+"SqlSessionTemplate");
                    scanner.setSqlSessionFactoryBeanName("");
                    scanner.registerFilters();
                    scanner.doScan(prop.get(Constant.MYBATIS_SCAN).split(","));
                }
            };
            DynamicDataSourceConfiguration.addMybatisConsumer(registry,userConsumer);
        } catch (Exception e) {
            throw new DynamicDataSourceException("mybatis-mapper-path配置出错，请检查");
        }

    }
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


}
