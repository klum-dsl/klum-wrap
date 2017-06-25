package com.blackbuild.klum.wrap;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface WrappedField {

    /**
     * The type of the factory.
     */
    Class<?> factory() default NoValue.class;

    interface NoValue {}
}
