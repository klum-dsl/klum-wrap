package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.expr.ClosureExpression;
import org.codehaus.groovy.ast.expr.Expression;

import static org.codehaus.groovy.ast.tools.GeneralUtils.callX;

public class ClosureBasedFactory extends WrappedFieldFactory {
    private final ClosureExpression closure;

    public ClosureBasedFactory(ClosureExpression closure) {
        super();
        this.closure = closure;
    }

    @Override
    public Expression fromDelegateX(Expression source) {
        return callX(closure, "call", source);
    }
}
