package com.blackbuild.klum.wrap.ast;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.wrap.ast.KlumWrapTransformation.DELEGATE_FIELD_NAME;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

public class SingleWrappedField extends FieldProvider {
    public SingleWrappedField(FieldNode field) {
        super(field);
    }

    @Override
    Statement getInitializationCode() {
        return assignS(attrX(varX("this"), constX(field.getName())), ctorX(field.getType(), propX(varX(DELEGATE_FIELD_NAME), field.getName())));
    }

    @Override
    Statement getGetterCode() {
        return returnS(attrX(varX("this"), constX(field.getName())));
    }
}
