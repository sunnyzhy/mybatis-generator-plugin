package org.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * @author zhy
 * @date 2021/3/31 9:36
 */
public class Swagger2Plugin extends PluginAdapter {
    public boolean validate(List<String> list) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        String useDefaultProperty = properties.getProperty("useDefault");
        String apiModelAnnotationPackage;
        String apiModelPropertyAnnotationPackage;
        boolean useDefault = (null == useDefaultProperty || Boolean.parseBoolean(useDefaultProperty));
        if (useDefault) { // 使用默认的配置
            apiModelAnnotationPackage = "io.swagger.annotations.ApiModel";
            apiModelPropertyAnnotationPackage = "io.swagger.annotations.ApiModelProperty";
        } else { // 使用插件里的配置
            // 读取配置文件 generatorConfig.xml 里 property 的 name 为 apiModel 的值
            apiModelAnnotationPackage = properties.getProperty("apiModel");
            if (apiModelAnnotationPackage == null) {
                System.out.println("没有找到apiModel的配置项，示例: <property name=\"apiModel\" value=\"io.swagger.annotations.ApiModel\"/>");
                return false;
            }
            apiModelAnnotationPackage = apiModelAnnotationPackage.trim();
            if (apiModelAnnotationPackage.length() == 0) {
                System.out.println("apiModel的配置项为空，示例: <property name=\"apiModel\" value=\"io.swagger.annotations.ApiModel\"/>");
                return false;
            }

            // 读取配置文件 generatorConfig.xml 里 property 的 name 为 apiModelProperty 的值
            apiModelPropertyAnnotationPackage = properties.getProperty("apiModelProperty");
            if (apiModelPropertyAnnotationPackage == null) {
                System.out.println("没有找到apiModelProperty的配置项，示例: <property name=\"apiModelProperty\" value=\"io.swagger.annotations.ApiModelProperty\"/>");
                return false;
            }
            apiModelPropertyAnnotationPackage = apiModelPropertyAnnotationPackage.trim();
            if (apiModelPropertyAnnotationPackage.length() == 0) {
                System.out.println("apiModelProperty的配置项为空，示例: <property name=\"apiModelProperty\" value=\"io.swagger.annotations.ApiModelProperty\"/>");
                return false;
            }
        }

        // import io.swagger.annotations.ApiModel;
        topLevelClass.addImportedType(apiModelAnnotationPackage);
        // 添加 @ApiModel 注解，description 是数据表的 comment
        String apiModelAnnotation = "@ApiModel(value=\"" + topLevelClass.getType().getShortName() + "\", description=\"" + introspectedTable.getRemarks() + "\")";
        if (!topLevelClass.getAnnotations().contains(apiModelAnnotation)) {
            topLevelClass.addAnnotation(apiModelAnnotation);
        }

        // import io.swagger.annotations.ApiModelProperty;
        topLevelClass.addImportedType(apiModelPropertyAnnotationPackage);
        // 添加 @ApiModelProperty 注解, value 是数据表字段的 comment
        field.addAnnotation("@ApiModelProperty(value=\"" + introspectedColumn.getRemarks() + "\")");
        return true;
    }
}
