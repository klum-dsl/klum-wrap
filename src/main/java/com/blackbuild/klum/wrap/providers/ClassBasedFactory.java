package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;

import static org.codehaus.groovy.ast.tools.GeneralUtils.callX;

public class ClassBasedFactory extends WrappedFieldFactory {

    private final ClassNode targetClass;
    private final String methodName;

    public ClassBasedFactory(ClassNode annotation, String methodName) {
        targetClass = annotation;
        this.methodName = methodName;
    }

    @Override
    public Expression fromDelegateX(Expression source) {
        return callX(targetClass, methodName, source);
    }
}
