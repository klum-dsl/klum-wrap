package com.blackbuild.klum.wrap.ast;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.common.CommonAstHelper.getElementType;
import static com.blackbuild.klum.common.CommonAstHelper.initializeCollectionOrMap;
import static com.blackbuild.klum.wrap.ast.WrapAstHelper.getWrappedTypeFor;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

public abstract class CollectionOrMapFieldProvider extends FieldProvider {

    protected final ClassNode elementType;
    protected final ClassNode wrappedType;

    CollectionOrMapFieldProvider(FieldNode field) {
        super(field);
        elementType = getElementType(field);
        wrappedType = getWrappedTypeFor(elementType);
    }

    @Override
    void modifyField() {
        super.modifyField();
        initializeCollectionOrMap(field);
    }

    @Override
    Statement getGetterCode() {
        MethodCallExpression callAsImmutable = callX(attrX(varX("this"), constX(field.getName())), "asImmutable");
        callAsImmutable.setSafe(true);
        return returnS(callAsImmutable);
    }
}
