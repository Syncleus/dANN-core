package com.syncleus.dann.annotation;

import java.lang.annotation.*;

/**
 * The equals method of any class taged with this annotation will remain consistent and hashCode() will always
 * return the same value on subsequent calls.
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Immutable
{
}
