package com.blackbuild.klum.wrap;

import groovy.transform.Undefined;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface WrappedField {

    /**
     * The type of the factory.
     */
    Class<?> factory() default Undefined.class;

    String method() default "create";
}
