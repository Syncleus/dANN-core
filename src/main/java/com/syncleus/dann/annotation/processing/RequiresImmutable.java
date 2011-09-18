package com.syncleus.dann.annotation.processing;

import com.syncleus.dann.annotation.Immutable;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Requires(Immutable.class)
public @interface RequiresImmutable
{
}
