package com.github.nicqiang.pointcloud.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h>注解Aspect</h>
 *
 * @author nicqiang
 * @since 2019-06-01
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CostLogAspect {
    String value() default "";

    String descript() default "耗时注解";
}
