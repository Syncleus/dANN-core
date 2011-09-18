package com.syncleus.dann.graph.annotation;

import java.lang.annotation.*;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FixedEndpoints
{
    int count() default -1;
}
