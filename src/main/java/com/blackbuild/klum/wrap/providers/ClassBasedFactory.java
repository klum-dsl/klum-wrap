package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;

import static org.codehaus.groovy.ast.tools.GeneralUtils.callX;

public class ClassBasedFactory extends WrappedFieldFactory {

    private final ClassNode targetClass;

    public ClassBasedFactory(ClassNode annotation) {
        targetClass = annotation;
    }

    @Override
    public Expression fromDelegateX(Expression source) {
        return callX(targetClass, "create", source);
    }
}
