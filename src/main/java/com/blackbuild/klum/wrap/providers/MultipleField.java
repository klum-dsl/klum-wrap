package com.blackbuild.klum.wrap.providers;

import com.blackbuild.klum.common.CommonAstHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.common.CommonAstHelper.initializeCollectionOrMap;
import static com.blackbuild.klum.wrap.ast.WrapAstHelper.getWrappedTypeFor;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

public abstract class MultipleField extends FieldHandler {

    protected final ClassNode elementType;
    protected final ClassNode wrappedType;

    public MultipleField(FieldNode field) {
        super(field);
        elementType = CommonAstHelper.getElementType(field);
        wrappedType = getWrappedTypeFor(elementType);
    }

    @Override
    public ClassNode getElementType() {
        return elementType;
    }

    @Override
    protected void doModifyField() {
        initializeCollectionOrMap(field);
    }

    @Override
    public Statement getGetterCode() {
        MethodCallExpression callAsImmutable = callX(attrX(varX("this"), constX(field.getName())), "asImmutable");
        callAsImmutable.setSafe(true);
        return returnS(callAsImmutable);
    }
}
