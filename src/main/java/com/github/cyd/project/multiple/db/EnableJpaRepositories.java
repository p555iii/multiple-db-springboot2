package com.github.cyd.project.multiple.db;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;
import org.springframework.data.repository.query.QueryLookupStrategy;

import java.lang.annotation.*;

/**
 * @Description jpa配置自定义注解
 * @Author changyandong
 * @Created Date: 2018/7/30 11:45
 * @ClassName EnableJpaRepositories
 * @Version: 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RepositoryRegistrarSupport.class})
public @interface EnableJpaRepositories {
    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};

    ComponentScan.Filter[] includeFilters() default {};

    ComponentScan.Filter[] excludeFilters() default {};

    String repositoryImplementationPostfix() default "Impl";

    String namedQueriesLocation() default "";

    QueryLookupStrategy.Key queryLookupStrategy() default QueryLookupStrategy.Key.CREATE_IF_NOT_FOUND;

    Class<?> repositoryFactoryBeanClass() default JpaRepositoryFactoryBean.class;

    Class<?> repositoryBaseClass() default DefaultRepositoryBaseClass.class;

    String entityManagerFactoryRef() default "entityManagerFactory";

    String transactionManagerRef() default "transactionManager";

    boolean considerNestedRepositories() default false;

    boolean enableDefaultTransactions() default true;
}
