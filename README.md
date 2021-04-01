# 自定义 mybatis-generator 插件

## 前言
由于 spring-boot:2.4.0 以上版本不兼容 maven-resources-plugin:3.2.0，所以示例中使用的是 spring-boot:2.3.6.RELEASE

## 内容
- Swagger2Plugin: 自动生成 swagger2 注解
- LombokPlugin: 自动生成 lombok 注解

## 使用方法
1. 安装插件到本地仓库
   ```bash
   mvn clean install -Dmaven.test.skip=true
   ```
2. 在业务工程里添加自定义的插件依赖
   ```xml
   <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        <version>1.3.6</version>
        <configuration>
            <configurationFile>${basedir}/src/main/resources/generator/generatorConfig.xml</configurationFile>
            <overwrite>true</overwrite>
            <verbose>true</verbose>
        </configuration>
        <dependencies>
            <!-- 省略其他依赖 -->
            
            <!-- 添加自定义的插件依赖 -->
            <dependency>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-plugin</artifactId>
                <version>0.0.1</version>
            </dependency>
        </dependencies>
    </plugin>
   ```
3. 在业务工程的配置文件 generatorConfig.xml 里添加自定义插件
   ```xml
   <context id="Mysql" targetRuntime="MyBatis3Simple" defaultModelType="flat">
        <!-- 省略其他配置 -->
        
        <!-- 自定义的 mybatis-generator-swagger2 插件 -->
        <plugin type="org.mybatis.generator.plugin.Swagger2Plugin"/>

        <!-- 自定义的 mybatis-generator-lombok 插件 -->
        <plugin type="org.mybatis.generator.plugin.LombokPlugin"/>

        <jdbcConnection>
            <!-- 设置 useInformationSchema 的值为 true，否则，IntrospectedTable 取到的表 comment 为空字符串 -->
            <property name="useInformationSchema" value="true" />
        </jdbcConnection>
   
        <!-- 省略其他配置 -->
   </context>
   ```