package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.expr.Expression;

abstract class ElementFactory {

    public abstract void modifyField();
    public abstract Expression fromDelegateX(Expression source);
}
