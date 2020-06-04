<p align="center">
	<strong>基于springboot2.0的配置式多数据源实现</strong>
</p>

<p align="center">
    <a>
        <img src="https://maven-badges.herokuapp.com/maven-central/com.baomidou/lock4j-spring-boot-starter/badge.svg" >
    </a>
    <a>
        <img src="https://img.shields.io/badge/JDK-1.8-green.svg" >
    </a>
    <a>
        <img src="https://img.shields.io/badge/springBoot-2.0-green.svg" >
    </a>
</p>

## 简介

multiple-db-springboot2 基于springboot2.0实现了 通过application配置文件实现 Mybatis + Jpa 多数据源 

## 如何使用

1.引入相关依赖。下载源码，执行 mvn clean install即可

```xml
<dependency>
    <groupId>com.github.cyd</groupId>
    <artifactId>multiple-db-springboot2</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
2.application配置文件。注意：primaryDataSource为默认的主数据源引用你项目的主数据源 可以不配置，不配置primaryDataSource的情况下必须有一个数据源名字为primary，配置中的mybatis-scan、model-package、jpa-scan、mapper-path支持通配符，支持配置多个，请用,分隔。如果你只使用jpa或者mybatis的其中一种，请不要配置另外一种的属性。例如，项目中不使用jpa那么jpa-model-package和jpa-scan就不要配置。

3.如果你的项目中只有一个主数据源同时使用jpa和mybatis 请在使用mybatis做增删改操作时注意在@Transactional注解上加上事务管理器的名字，事务管理器的名字是transactionManagerPrimary4+数据源的名字
```java
@Transactional(rollbackFor = Exception.class,value = "transactionManagerPrimary4api")
    public void updateInterfaceInfo(ApiInterfaceInfoEntity entity) {
        apiInterfaceInfoMapper.updateInterfaceInfo(entity);
        int i = 4/0;
    }
```
4.这里留一份当前支持的比较全的一份配置，使用的是properties格式的文件
```prop
#主数据源只需要一个
spring.primaryDataSource=spring.datasource.base
spring.datasource.base.url=jdbc:mysql://192.168.8.86:3306/cyd_test?characterEncoding=UTF-8&useSSL=false
spring.datasource.base.username=root
spring.datasource.base.password=****
spring.datasource.base.driverClassName=com.mysql.jdbc.Driver
spring.datasource.base.jpa-model-package=com.github.cyd.project.common.module.base.user.po
spring.datasource.base.jpa-scan=com.github.cyd.project.common.module.base.user.dao
spring.datasource.base.mybatis-scan=com.github.cyd.project.common.module.base.user.mapper
spring.datasource.base.mybatis-mapper-path=/mapper/**/*.xml
#从这里开始的所有配置是非必填的
spring.datasource.base.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000
spring.datasource.base.testWhileIdle=true
spring.datasource.base.maxActive=10
spring.datasource.base.minIdle=2
spring.datasource.base.initialSize=2
spring.datasource.base.removeAbandoned=true
spring.datasource.base.removeAbandonedTimeout=280
spring.datasource.base.logAbandoned=true
spring.datasource.base.validationQuery=select 1
spring.jpa.show-sql=true
```
5.如果你的项目中有多个数据源，非主数据源也使用了jpa或者mybatis时，同样请在使用mybatis或jpa做增删改操作时注意在@Transactional注解上加上事务管理器的名字，事务管理器的名字mybatis为数据源名字+TransactionManager例如apiTransactionManager，jpa事务管理器名字为transactionManagerPrimary4+数据源名例如transactionManagerPrimary4api
## 计划
:smile: