package com.blackbuild.klum.wrap.ast;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.GenericsType;
import org.codehaus.groovy.ast.stmt.ForStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.ast.tools.GenericsUtils;

import java.util.Map;

import static com.blackbuild.klum.wrap.ast.KlumWrapTransformation.DELEGATE_FIELD_NAME;
import static org.codehaus.groovy.ast.ClassHelper.make;
import static org.codehaus.groovy.ast.tools.GeneralUtils.*;

public class WrappedMapField extends CollectionOrMapFieldProvider {

    public WrappedMapField(FieldNode field) {
        super(field);
    }

    @Override
    Statement getInitializationCode() {
        return                             new ForStatement(
                param(GenericsUtils.makeClassSafeWithGenerics(make(Map.Entry.class), field.getType().getGenericsTypes()[0], new GenericsType(wrappedType)), "$next"),
                propX(varX(DELEGATE_FIELD_NAME), field.getName()),
                stmt(callX(varX(field.getName()), "put", args(propX(varX("$next"), "key"), ctorX(elementType, propX(varX("$next"), "value")))))
        );
    }
}
