package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;

import static com.blackbuild.klum.wrap.ast.WrapAstHelper.getWrappedTypeFor;
import static org.codehaus.groovy.ast.tools.GeneralUtils.ctorX;

public class BasicWrap extends ElementFactory {

    private final ClassNode type;

    public BasicWrap(ClassNode type) {
        this.type = type;
    }

    static boolean enrichIfValid(FieldHandler result) {
        ClassNode wrappedType = getWrappedTypeFor(result.getElementType());
        if (wrappedType != null) {
            result.setFactory(new BasicWrap(result.getElementType()));
            return true;
        }
        return false;
    }

    @Override
    public void modifyField() {

    }

    @Override
    public Expression fromDelegateX(Expression source) {
        return ctorX(type, source);
    }

}
