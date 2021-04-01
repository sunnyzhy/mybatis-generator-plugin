package org.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author zhy
 * @date 2021/3/31 9:35
 */
public class LombokPlugin extends PluginAdapter {
    @Override
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (lombokEnabled()) {
            // import lombok.Data;
            topLevelClass.addImportedType("lombok.Data");
            // 添加 @Data 注解
            topLevelClass.addAnnotation("@Data");
        }
        // 必须返回 true，否则不会生成表的实体类
        return true;
    }


    @Override
    public boolean modelSetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !lombokEnabled();
    }

    @Override
    public boolean modelGetterMethodGenerated(Method method, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        return !lombokEnabled();
    }

    private boolean lombokEnabled() {
        // 读取配置文件 generatorConfig.xml 里 property 的 name 为 lombokEnabled 的值
        String lombokEnabledProperty = properties.getProperty("lombokEnabled");
        return (null == lombokEnabledProperty || Boolean.parseBoolean(lombokEnabledProperty));
    }
}
