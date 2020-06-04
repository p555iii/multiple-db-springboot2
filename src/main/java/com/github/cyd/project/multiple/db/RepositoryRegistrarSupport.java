package com.github.cyd.project.multiple.db;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.*;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @Description jpa 扫描机注册
 * @Author changyandong
 * @Created Date: 2018/7/30 10:32
 * @ClassName E6RepositoryRegistrarSupport
 * @Version: 1.0
 */
public class RepositoryRegistrarSupport extends RepositoryBeanDefinitionRegistrarSupport {
    private ResourceLoader resourceLoader;
    private Environment environment;


    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        try {
            Consumer<BeanDefinitionRegistry> userConsumer = jpaRegistry -> {
                for (Map.Entry<String, Map<String,String>> entry : DynamicDataSourceConfiguration.datasource.entrySet()) {
                    String dataSourceName = entry.getKey();
                    Map<String,String> prop = entry.getValue();
                    if(Objects.equals(prop.get(Constant.JPA_USE),Boolean.FALSE.toString())){
                        continue;
                    }
                    DynamicDataSourceConfiguration.setDefaultJPAScanAndPackage(dataSourceName,prop);
                    Assert.notNull(this.resourceLoader, "ResourceLoader must not be null!");
                    Assert.notNull(annotationMetadata, "AnnotationMetadata must not be null!");
                    Assert.notNull(registry, "BeanDefinitionRegistry must not be null!");
                    if (annotationMetadata.getAnnotationAttributes(this.getAnnotation().getName()) != null) {
                        AnnotationRepositoryConfigurationSource configurationSource = new AnnotationRepositoryConfigurationSource(annotationMetadata, this.getAnnotation(), this.resourceLoader, this.environment, registry);
                        AnnotationAttributes attributes = configurationSource.getAttributes();
                        attributes.put("entityManagerFactoryRef", "entityManagerFactoryPrimary4"+dataSourceName);
                        attributes.put("transactionManagerRef", "transactionManagerPrimary4"+dataSourceName);

                        String[] str =prop.get(Constant.JPA_SCAN).split(",");
                        attributes.put("basePackages", str);
                        RepositoryConfigurationExtension extension = this.getExtension();
                        RepositoryConfigurationUtils.exposeRegistration(extension, registry, configurationSource);
                        RepositoryConfigurationDelegate delegate = new RepositoryConfigurationDelegate(configurationSource, this.resourceLoader, this.environment);
                        delegate.registerRepositoriesIn(registry, extension);
                    }
                }
            };
            DynamicDataSourceConfiguration.addJpaConsumer(registry,userConsumer);
        } catch (Exception e) {
            throw new DynamicDataSourceException("jpa-scan配置出错，请检查");
        }
    }
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableJpaRepositories.class;
    }
    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new JpaRepositoryConfigExtension();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}
