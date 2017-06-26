package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;

import static org.codehaus.groovy.ast.tools.GeneralUtils.ctorX;

public class BasicWrap extends ElementFactory {

    private final ClassNode type;

    public BasicWrap(ClassNode type) {
        this.type = type;
    }

    @Override
    public void modifyField() {

    }

    @Override
    public Expression fromDelegateX(Expression source) {
        return ctorX(type, source);
    }

}
