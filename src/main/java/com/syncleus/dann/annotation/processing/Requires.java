package com.syncleus.dann.annotation.processing;

import java.lang.annotation.*;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public abstract @interface Requires
{
    Class<? extends Annotation>[] value();
}
