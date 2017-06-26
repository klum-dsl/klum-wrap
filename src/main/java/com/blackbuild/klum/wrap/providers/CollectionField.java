package com.blackbuild.klum.wrap.providers;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.Statement;

import static com.blackbuild.klum.wrap.ast.KlumWrapTransformation.DELEGATE_FIELD_NAME;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

public class CollectionField extends MultipleField {

    public CollectionField(FieldNode field) {
        super(field);
    }

    @Override
    public Statement initializeWrapperFieldS() {
        return new ForStatement(
                param(wrappedType, "$next"),
                propX(varX(DELEGATE_FIELD_NAME), field.getName()),
                stmt(callX(varX(field.getName()), "add", factory.fromDelegateX(varX("$next"))))
        );
    }
}
