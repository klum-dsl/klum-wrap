package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.wrap.ast.KlumWrapTransformation.DELEGATE_FIELD_NAME;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

public class SingleField extends FieldHandler {

    public SingleField(FieldNode field) {
        super(field);
    }

    @Override
    public Statement initializeWrapperFieldS() {
        return assignS(
                attrX(varX("this"), constX(field.getName())),
                factory.fromDelegateX(propX(varX(DELEGATE_FIELD_NAME), field.getName()))
        );
    }

    @Override
    public Statement getGetterCode() {
        return returnS(attrX(varX("this"), constX(field.getName())));
    }

    @Override
    public ClassNode getElementType() {
        return field.getType();
    }

    @Override
    protected void doModifyField() {

    }
}